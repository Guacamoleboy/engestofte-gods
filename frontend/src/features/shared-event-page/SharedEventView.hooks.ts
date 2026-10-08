// Pathing
// _______
// src/features/shared-event-page/SharedEventView.hooks.ts

import { useCallback, useEffect, useRef, useState, type FormEvent } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { addEventContact, addOwnerEventContact, closeEvent as closeCustomerEvent, closeOwnerEvent, createEventInvitation, decideEventChange, decideOwnerEventChange, getEvent, getEventChangeProposals, getEventMessages, getOwnerEvent, getOwnerEventChangeProposals, getOwnerEventMessages, getStaffEvent, payEventDeposit, proposeEventChange, proposeOwnerEventChange, requestOwnerEventDeposit, sendEventMessage, sendOwnerEventMessage, sendStaffEventMessage, type ChangeProposal, type EventMessage } from '../../api/endpoints/events'
import { useAuth } from '../../shared/hooks/useAuth'

export type SharedEventInfo = {
	eventId: number
	eventName: string | null
	customerName: string | null
	primaryContactName: string | null
	customerEmail: string | null
	expectedGuestCount: number | null
	requestedDate: string | null
	hasAllergies: boolean | null
	allergyDetails: string | null
	expectedVeganCount: number | null
	weddingDirection: number | null
	approvedAt: string
	status: 'APPROVED' | 'FOLLOW_UP_REQUIRED' | 'OWNER_FOLLOW_UP_REQUIRED' | 'AWAITING_APPROVAL' | 'AWAITING_DEPOSIT' | 'BOOKED' | 'CLOSED_BY_CUSTOMER' | 'CLOSED_BY_OWNER' | 'CANCELLED_BY_CUSTOMER'
	guestInvitationCreated: boolean
}

type EditableEventField = 'event_name' | 'expected_guest_count' | 'requested_date' | 'expected_vegan_count' | 'has_allergies' | 'allergy_details' | 'wedding_direction'

const emptyProposalValues: Record<EditableEventField, string> = { event_name: '', expected_guest_count: '', requested_date: '', expected_vegan_count: '', has_allergies: '', allergy_details: '', wedding_direction: '' }

export function useSharedEventView() {
	const { id: rawId } = useParams()
	const navigate = useNavigate()
	const { user } = useAuth()
	const id = Number(rawId)
	const [event, setEvent] = useState<SharedEventInfo | null>(null)
	const [messages, setMessages] = useState<EventMessage[]>([])
	const [message, setMessage] = useState('')
	const [state, setState] = useState<'loading' | 'loaded' | 'error'>('loading')
	const [messageState, setMessageState] = useState<'idle' | 'sending' | 'sent' | 'error'>('idle')
	const [contactEmail, setContactEmail] = useState('')
	const [contactState, setContactState] = useState<'idle' | 'adding' | 'added' | 'error'>('idle')
	const [closeState, setCloseState] = useState<'idle' | 'closing' | 'error'>('idle')
	const [depositState, setDepositState] = useState<'idle' | 'sending' | 'error'>('idle')
	const [invitationState, setInvitationState] = useState<'idle' | 'opening' | 'error'>('idle')
	const [invitationUrl, setInvitationUrl] = useState<string | null>(null)
	const [invitationCopyState, setInvitationCopyState] = useState<'idle' | 'copied' | 'error'>('idle')
	const [isPrimaryContact, setIsPrimaryContact] = useState(false)
	const [proposals, setProposals] = useState<ChangeProposal[]>([])
	const [proposalValues, setProposalValues] = useState<Record<EditableEventField, string>>(emptyProposalValues)
	const [proposalTouched, setProposalTouched] = useState<Partial<Record<EditableEventField, boolean>>>({})
	const [proposalState, setProposalState] = useState<'idle' | 'submitting' | 'error'>('idle')
	const [rejectingProposalId, setRejectingProposalId] = useState<number | null>(null)
	const [rejectionExplanation, setRejectionExplanation] = useState('')
	const [decisionState, setDecisionState] = useState<'idle' | 'submitting' | 'error'>('idle')
	const messagesContainerRef = useRef<HTMLDivElement>(null)

	const loadEvent = useCallback(async () => {
		if (!Number.isInteger(id) || id < 1 || !user) {
			setState('error')
			return
		}
		setState('loading')
		try {
			if (user.role === 'OWNER') {
				const [ownerEvent, ownerMessages, ownerProposals] = await Promise.all([getOwnerEvent(id), getOwnerEventMessages(id), getOwnerEventChangeProposals(id)])
				setEvent({ eventId: ownerEvent.event_id, eventName: ownerEvent.event_name, customerName: ownerEvent.customer_name, primaryContactName: ownerEvent.primary_contact_name, customerEmail: ownerEvent.customer_email_redacted, expectedGuestCount: ownerEvent.expected_guest_count, requestedDate: ownerEvent.requested_date, hasAllergies: ownerEvent.has_allergies, allergyDetails: ownerEvent.allergy_details, expectedVeganCount: ownerEvent.expected_vegan_count, weddingDirection: ownerEvent.wedding_direction, approvedAt: ownerEvent.approved_at, status: ownerEvent.status, guestInvitationCreated: false })
				setProposalValues(emptyProposalValues)
				setProposalTouched({})
				setIsPrimaryContact(false)
				setMessages(ownerMessages)
				setProposals(ownerProposals)
			} else if (user.role === 'CUSTOMER') {
				const customerEvent = await getEvent(id)
				if (!customerEvent.approved_at) {
					navigate(`/dashboard/approval/${id}`, { replace: true })
					return
				}
				const [customerMessages, customerProposals] = await Promise.all([getEventMessages(id), getEventChangeProposals(id)])
				setEvent({
					eventId: customerEvent.event_id,
					eventName: customerEvent.event_data.event_name ?? null,
					customerName: customerEvent.event_data.customer_name ?? null,
					primaryContactName: customerEvent.primary_contact_name,
					customerEmail: customerEvent.customer_email_redacted,
					expectedGuestCount: customerEvent.event_data.expected_guest_count ?? null,
					requestedDate: customerEvent.event_data.requested_date ?? null,
					hasAllergies: customerEvent.event_data.has_allergies ?? null,
					allergyDetails: customerEvent.event_data.allergy_details ?? null,
					expectedVeganCount: customerEvent.event_data.expected_vegan_count ?? null,
					weddingDirection: customerEvent.event_data.wedding_direction ?? null,
					approvedAt: customerEvent.approved_at,
					guestInvitationCreated: customerEvent.guest_invitation_created,
					status: customerEvent.status === 'AWAITING_APPROVAL' ? 'AWAITING_APPROVAL'
						: customerEvent.status === 'AWAITING_DEPOSIT' ? 'AWAITING_DEPOSIT'
							: customerEvent.status === 'BOOKED' ? 'BOOKED'
						: customerEvent.status === 'CLOSED_BY_CUSTOMER' || customerEvent.status === 'CLOSED_BY_OWNER' || customerEvent.status === 'CANCELLED_BY_CUSTOMER'
							? customerEvent.status : 'APPROVED',
				})
				setProposalValues(emptyProposalValues)
				setProposalTouched({})
				setIsPrimaryContact(customerEvent.is_primary_contact)
				setMessages(customerMessages)
				setProposals(customerProposals)
			} else {
				const staffEvent = await getStaffEvent(id)
				setEvent({ eventId: staffEvent.event_id, eventName: staffEvent.event_name, customerName: staffEvent.customer_name, primaryContactName: null, customerEmail: null, expectedGuestCount: staffEvent.expected_guest_count, requestedDate: staffEvent.requested_date, hasAllergies: staffEvent.has_allergies, allergyDetails: staffEvent.allergy_details, expectedVeganCount: staffEvent.expected_vegan_count, weddingDirection: staffEvent.wedding_direction, approvedAt: staffEvent.approved_at, status: staffEvent.status, guestInvitationCreated: false })
				setMessages([])
				setProposals([])
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
		if (!message.trim() || !user) return
		setMessageState('sending')
		try {
			const sentMessage = user.role === 'OWNER'
				? await sendOwnerEventMessage(id, message)
				: user.role === 'STAFF'
					? await sendStaffEventMessage(id, message)
					: await sendEventMessage(id, message)
			setMessages((current) => [...current, sentMessage])
			setMessage('')
			setMessageState('sent')
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

	const closeCurrentEvent = useCallback(async () => {
		if (!user || (user.role !== 'OWNER' && !isPrimaryContact)) return
		setCloseState('closing')
		try {
			if (user.role === 'OWNER') {
				await closeOwnerEvent(id)
				navigate('/owner/requests', { replace: true })
			} else {
				await closeCustomerEvent(id)
				setCloseState('idle')
				await loadEvent()
			}
		} catch {
			setCloseState('error')
		}
	}, [id, isPrimaryContact, loadEvent, navigate, user])

	const requestDeposit = useCallback(async () => {
		if (!user || user.role !== 'OWNER' || event?.status !== 'APPROVED') return
		setDepositState('sending')
		try {
			await requestOwnerEventDeposit(id)
			setDepositState('idle')
			await loadEvent()
		} catch {
			setDepositState('error')
		}
	}, [event?.status, id, loadEvent, user])

	const payDeposit = useCallback(async () => {
		if (!user || user.role !== 'CUSTOMER' || !isPrimaryContact || event?.status !== 'AWAITING_DEPOSIT') return
		setDepositState('sending')
		try {
			await payEventDeposit(id)
			setDepositState('idle')
			await loadEvent()
		} catch {
			setDepositState('error')
		}
	}, [event?.status, id, isPrimaryContact, loadEvent, user])

	const openInvitation = useCallback(() => {
		if (!user || user.role !== 'CUSTOMER' || !isPrimaryContact || event?.status !== 'BOOKED') return
		const invitationWindow = window.open('about:blank', '_blank')
		setInvitationState('opening')
		void (async () => {
			try {
				const { access } = await createEventInvitation(id)
				const url = new URL(`/events/${id}`, window.location.origin)
				url.searchParams.set('access', access)
				setInvitationUrl(url.toString())
				setEvent((current) => current ? { ...current, guestInvitationCreated: true } : current)
				if (invitationWindow) {
					invitationWindow.opener = null
					invitationWindow.location.replace(url.toString())
				}
				setInvitationState('idle')
			} catch {
				invitationWindow?.close()
				setInvitationState('error')
			}
		})()
	}, [event?.status, id, isPrimaryContact, user])

	const copyInvitationLink = useCallback(async () => {
		if (!invitationUrl) return
		try {
			await navigator.clipboard.writeText(invitationUrl)
			setInvitationCopyState('copied')
		} catch {
			setInvitationCopyState('error')
		}
	}, [invitationUrl])

	const setProposalValue = useCallback((fieldName: EditableEventField, value: string) => {
		setProposalValues((current) => ({ ...current, [fieldName]: value }))
		setProposalTouched((current) => ({ ...current, [fieldName]: value === '' && (fieldName === 'has_allergies' || fieldName === 'wedding_direction') ? false : true }))
		if (fieldName === 'has_allergies' && value === 'false') {
			setProposalValues((current) => ({ ...current, allergy_details: '' }))
			setProposalTouched((current) => ({ ...current, allergy_details: false }))
		}
	}, [])

	const changedProposalFields = (Object.keys(proposalValues) as EditableEventField[]).filter((fieldName) => {
		if (!proposalTouched[fieldName]) return false
		const currentValue = fieldName === 'event_name' ? event?.eventName ?? 'Bryllupsevent'
			: fieldName === 'expected_guest_count' ? event?.expectedGuestCount == null ? '' : String(event.expectedGuestCount)
				: fieldName === 'requested_date' ? event?.requestedDate ?? ''
					: fieldName === 'expected_vegan_count' ? event?.expectedVeganCount == null ? '' : String(event.expectedVeganCount)
						: fieldName === 'has_allergies' ? event?.hasAllergies == null ? '' : String(event.hasAllergies)
							: fieldName === 'allergy_details' ? event?.allergyDetails ?? ''
								: event?.weddingDirection == null ? '' : String(event.weddingDirection)
		const proposedValue = proposalValues[fieldName].trim()
		return (fieldName === 'allergy_details' || proposedValue !== '') && proposedValue !== currentValue
	})
	const hasProposalChanges = changedProposalFields.length > 0

	const submitEventProposals = useCallback(async () => {
		if (!hasProposalChanges || !user || (user.role !== 'OWNER' && !isPrimaryContact)) return
		setProposalState('submitting')
		try {
			for (const fieldName of changedProposalFields) {
				const newValue = proposalValues[fieldName].trim()
				const proposal = user.role === 'OWNER'
					? await proposeOwnerEventChange(id, fieldName, newValue)
					: await proposeEventChange(id, fieldName, newValue)
				setProposals((current) => [proposal, ...current])
				setProposalValues((current) => ({ ...current, [fieldName]: '' }))
				setProposalTouched((current) => ({ ...current, [fieldName]: false }))
			}
			setProposalState('idle')
			await loadEvent()
		} catch {
			setProposalState('error')
		}
	}, [changedProposalFields, hasProposalChanges, id, isPrimaryContact, loadEvent, proposalValues, user])

	const decideProposal = useCallback(async (proposalId: number, decision: 'APPROVED' | 'REJECTED', explanation?: string) => {
		if (!user || (user.role !== 'OWNER' && !isPrimaryContact)) return
		setDecisionState('submitting')
		try {
			const proposal = user.role === 'OWNER'
				? await decideOwnerEventChange(id, proposalId, decision, explanation)
				: await decideEventChange(id, proposalId, decision, explanation)
			setProposals((current) => current.map((item) => item.id === proposal.id ? proposal : item))
			setRejectingProposalId(null)
			setRejectionExplanation('')
			setDecisionState('idle')
			await loadEvent()
		} catch {
			setDecisionState('error')
		}
	}, [id, isPrimaryContact, loadEvent, user])

	return { addContact, closeCurrentEvent, closeState, contactEmail, contactState, copyInvitationLink, decideProposal, decisionState, depositState, event, hasProposalChanges, invitationCopyState, invitationState, invitationUrl, isPrimaryContact, loadEvent, message, messageState, messages, messagesContainerRef, openInvitation, payDeposit, proposalState, proposalValues, proposals, rejectionExplanation, rejectingProposalId, requestDeposit, role: user?.role ?? null, sendMessage, setContactEmail, setMessage, setProposalValue, setRejectionExplanation, setRejectingProposalId, state, submitEventProposals }
}
