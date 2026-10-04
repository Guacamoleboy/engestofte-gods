// Pathing
// _______
// src/features/shared-event-page/SharedEventView.hooks.ts

import { useCallback, useEffect, useState, type FormEvent } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { getEvent, getEventMessages, getOwnerEvent, getOwnerEventMessages, getStaffEvent, sendEventMessage, sendOwnerEventMessage, type EventMessage } from '../../api/endpoints/events'
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

	const loadEvent = useCallback(async () => {
		if (!Number.isInteger(id) || id < 1 || !user) {
			setState('error')
			return
		}
		setState('loading')
		try {
			if (user.role === 'OWNER') {
				const [ownerEvent, ownerMessages] = await Promise.all([getOwnerEvent(id), getOwnerEventMessages(id)])
				setEvent({ eventId: ownerEvent.event_id, customerName: ownerEvent.customer_name, customerEmail: ownerEvent.customer_email, expectedGuestCount: ownerEvent.expected_guest_count, requestedDate: ownerEvent.requested_date, approvedAt: ownerEvent.approved_at })
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
					customerEmail: user.email || null,
					expectedGuestCount: customerEvent.event_data.expected_guest_count ?? null,
					requestedDate: customerEvent.event_data.requested_date ?? null,
					approvedAt: customerEvent.approved_at,
				})
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

	return { event, loadEvent, message, messageState, messages, role: user?.role ?? null, sendMessage, setMessage, state }
}
