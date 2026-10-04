// Pathing
// _______
// src/features/customer-approval-page/CustomerApprovalView.hooks.ts

import { useCallback, useEffect, useState, type FormEvent } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { ApiError } from '../../api/client'
import { getMyEnquiries, closeMyEnquiry, type EnquirySummary } from '../../api/endpoints/myEnquiries'
import { closeEvent, getEvent, getEventMessages, sendEventMessage, type CustomerEvent, type EventMessage } from '../../api/endpoints/events'

export function useCustomerApprovalView() {
	const { id: rawId } = useParams()
	const navigate = useNavigate()
	const id = Number(rawId)
	const submissionId = rawId ?? ''
	const [event, setEvent] = useState<CustomerEvent | null>(null)
	const [enquiry, setEnquiry] = useState<EnquirySummary | null>(null)
	const [messages, setMessages] = useState<EventMessage[]>([])
	const [message, setMessage] = useState('')
	const [state, setState] = useState<'loading' | 'loaded' | 'error' | 'unauthenticated'>('loading')
	const [messageState, setMessageState] = useState<'idle' | 'sending' | 'error'>('idle')
	const [closeState, setCloseState] = useState<'idle' | 'closing' | 'error'>('idle')

	const loadEvent = useCallback(async () => {
		if (!submissionId) {
			setState('error')
			return
		}
		setState('loading')
		try {
			const summary = (await getMyEnquiries()).find((item) => item.submission_id === submissionId || item.event_id === id)
			if (!summary) throw new Error('Enquiry not found')
			setEnquiry(summary)
			if (summary.status === 'APPROVED' && summary.event_id) {
				navigate(`/dashboard/events/${summary.event_id}`, { replace: true })
				return
			}
			if (summary.event_id) {
				const [loadedEvent, loadedMessages] = await Promise.all([getEvent(summary.event_id), getEventMessages(summary.event_id)])
				if (loadedEvent.approved_at) {
					navigate(`/dashboard/events/${summary.event_id}`, { replace: true })
					return
				}
				setEvent(loadedEvent)
				setMessages(loadedMessages)
			} else {
				setEvent(null)
				setMessages([])
			}
			setState('loaded')
		} catch (error) {
			setState(error instanceof ApiError && (error.status === 401 || error.status === 403) ? 'unauthenticated' : 'error')
		}
	}, [id, navigate, submissionId])

	useEffect(() => { void loadEvent() }, [loadEvent])

	const sendMessage = useCallback(async (formEvent: FormEvent<HTMLFormElement>) => {
		formEvent.preventDefault()
		if (!message.trim() || !event) return
		setMessageState('sending')
		try {
			const sentMessage = await sendEventMessage(event.event_id, message)
			setMessages((current) => [...current, sentMessage])
			setMessage('')
			setMessageState('idle')
			setEnquiry((current) => current ? { ...current, status: 'OWNER_FOLLOW_UP_REQUIRED' } : current)
			setEvent((current) => current ? { ...current, status: 'OWNER_FOLLOW_UP_REQUIRED' } : current)
		} catch {
			setMessageState('error')
		}
	}, [event, message])

	const closeRequest = useCallback(async () => {
		setCloseState('closing')
		try {
			if (event) setEvent(await closeEvent(event.event_id))
			else await closeMyEnquiry(submissionId)
			setEnquiry((current) => current ? { ...current, status: 'CLOSED_BY_CUSTOMER' } : current)
			setCloseState('idle')
		} catch {
			setCloseState('error')
		}
	}, [event, submissionId])

	return { closeRequest, closeState, enquiry, event, loadEvent, message, messageState, messages, sendMessage, setMessage, state }
}
