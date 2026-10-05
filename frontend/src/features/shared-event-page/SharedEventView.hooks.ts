// Pathing
// _______
// src/features/shared-event-page/SharedEventView.hooks.ts

import { useCallback, useEffect, useRef, useState, type FormEvent } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { addEventContact, addOwnerEventContact, getEvent, getEventMessages, getOwnerEvent, getOwnerEventMessages, getStaffEvent, sendEventMessage, sendOwnerEventMessage, type EventMessage } from '../../api/endpoints/events'
import { useAuth } from '../../shared/hooks/useAuth'

export type SharedEventInfo = {
	eventId: number
	customerName: string | null
	customerEmail: string | null
	expectedGuestCount: number | null
	requestedDate: string | null
	approvedAt: string
}

export function useSharedEventView() {
	const { id: rawId } = useParams()
	const navigate = useNavigate()
	const { user } = useAuth()
	const id = Number(rawId)
	const [event, setEvent] = useState<SharedEventInfo | null>(null)
	const [messages, setMessages] = useState<EventMessage[]>([])
	const [message, setMessage] = useState('')
	const [state, setState] = useState<'loading' | 'loaded' | 'error'>('loading')
	const [messageState, setMessageState] = useState<'idle' | 'sending' | 'error'>('idle')
	const [contactEmail, setContactEmail] = useState('')
	const [contactState, setContactState] = useState<'idle' | 'adding' | 'added' | 'error'>('idle')
	const [isPrimaryContact, setIsPrimaryContact] = useState(false)
	const messagesContainerRef = useRef<HTMLDivElement>(null)

	const loadEvent = useCallback(async () => {
		if (!Number.isInteger(id) || id < 1 || !user) {
			setState('error')
			return
		}
		setState('loading')
		try {
			if (user.role === 'OWNER') {
				const [ownerEvent, ownerMessages] = await Promise.all([getOwnerEvent(id), getOwnerEventMessages(id)])
				setEvent({ eventId: ownerEvent.event_id, customerName: ownerEvent.customer_name, customerEmail: ownerEvent.customer_email_redacted, expectedGuestCount: ownerEvent.expected_guest_count, requestedDate: ownerEvent.requested_date, approvedAt: ownerEvent.approved_at })
				setIsPrimaryContact(false)
				setMessages(ownerMessages)
			} else if (user.role === 'CUSTOMER') {
				const customerEvent = await getEvent(id)
				if (!customerEvent.approved_at) {
					navigate(`/dashboard/approval/${id}`, { replace: true })
					return
				}
				const customerMessages = await getEventMessages(id)
				setEvent({
					eventId: customerEvent.event_id,
					customerName: customerEvent.event_data.customer_name ?? null,
					customerEmail: customerEvent.customer_email_redacted,
					expectedGuestCount: customerEvent.event_data.expected_guest_count ?? null,
					requestedDate: customerEvent.event_data.requested_date ?? null,
					approvedAt: customerEvent.approved_at,
				})
				setIsPrimaryContact(customerEvent.is_primary_contact)
				setMessages(customerMessages)
			} else {
				const staffEvent = await getStaffEvent(id)
				setEvent({ eventId: staffEvent.event_id, customerName: null, customerEmail: null, expectedGuestCount: staffEvent.expected_guest_count, requestedDate: staffEvent.requested_date, approvedAt: staffEvent.approved_at })
				setMessages([])
			}
			setState('loaded')
		} catch {
			setState('error')
		}
	}, [id, navigate, user])

	useEffect(() => { void loadEvent() }, [loadEvent])

	useEffect(() => {
		const container = messagesContainerRef.current
		if (container) container.scrollTop = container.scrollHeight
	}, [messages])

	const sendMessage = useCallback(async (formEvent: FormEvent<HTMLFormElement>) => {
		formEvent.preventDefault()
		if (!message.trim() || !user || user.role === 'STAFF') return
		setMessageState('sending')
		try {
			const sentMessage = user.role === 'OWNER'
				? await sendOwnerEventMessage(id, message)
				: await sendEventMessage(id, message)
			setMessages((current) => [...current, sentMessage])
			setMessage('')
			setMessageState('idle')
		} catch {
			setMessageState('error')
		}
	}, [id, message, user])

	const addContact = useCallback(async (formEvent: FormEvent<HTMLFormElement>) => {
		formEvent.preventDefault()
		if (!contactEmail.trim() || !user || (user.role !== 'OWNER' && !isPrimaryContact)) return
		setContactState('adding')
		try {
			if (user.role === 'OWNER') await addOwnerEventContact(id, contactEmail.trim())
			else await addEventContact(id, contactEmail.trim())
			setContactEmail('')
			setContactState('added')
		} catch {
			setContactState('error')
		}
	}, [contactEmail, id, isPrimaryContact, user])

	return { addContact, contactEmail, contactState, event, isPrimaryContact, loadEvent, message, messageState, messages, messagesContainerRef, role: user?.role ?? null, sendMessage, setContactEmail, setMessage, state }
}
