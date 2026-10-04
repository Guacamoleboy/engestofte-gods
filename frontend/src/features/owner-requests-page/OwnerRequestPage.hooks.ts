// Pathing
// _______
// src/features/owner-requests-page/OwnerRequestPage.hooks.ts

import { useCallback, useEffect, useRef, useState, type FormEvent } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { approveOwnerEnquiry, closeOwnerEnquiry, getOwnerEnquiry, getOwnerEnquiryMessages, sendOwnerEnquiryMessage, type EventMessage, type OwnerEnquiryReview } from '../../api/endpoints/ownerEnquiries'

export function useOwnerEnquiryReview() {
	const { id: rawId } = useParams()
	const navigate = useNavigate()
	const id = Number(rawId)
	const [review, setReview] = useState<OwnerEnquiryReview | null>(null)
	const [messages, setMessages] = useState<EventMessage[]>([])
	const messagesRequestId = useRef(0)
	const [message, setMessage] = useState('')
	const [state, setState] = useState<'loading' | 'loaded' | 'error'>('loading')
	const [approvalState, setApprovalState] = useState<'idle' | 'approving' | 'error'>('idle')
	const [messageState, setMessageState] = useState<'idle' | 'sending' | 'error'>('idle')
	const [rejectState, setRejectState] = useState<'idle' | 'rejecting' | 'rejected' | 'error'>('idle')

	const loadReview = useCallback(async () => {
		if (!Number.isInteger(id) || id < 1) {
			setState('error')
			return
		}
		setState('loading')
		try {
			setReview(await getOwnerEnquiry(id))
			setState('loaded')
		} catch {
			setState('error')
		}
	}, [id])

	const loadMessages = useCallback(async () => {
		const requestId = ++messagesRequestId.current
		try {
			const loadedMessages = await getOwnerEnquiryMessages(id)
			if (requestId === messagesRequestId.current) setMessages(loadedMessages)
		} catch {
			return
		}
	}, [id])

	useEffect(() => { void loadReview() }, [loadReview])
	useEffect(() => { void loadMessages() }, [loadMessages])

	const approve = useCallback(async () => {
		setApprovalState('approving')
		try {
			const approval = await approveOwnerEnquiry(id, '')
			navigate(`/owner/events/${approval.event_id}`, { replace: true })
		} catch {
			setApprovalState('error')
		}
	}, [id, navigate])

	const sendMessage = useCallback(async (event: FormEvent<HTMLFormElement>) => {
		event.preventDefault()
		if (!message.trim()) return
		setMessageState('sending')
		try {
			const sentMessage = await sendOwnerEnquiryMessage(id, message)
			messagesRequestId.current += 1
			setMessages((current) => current.some((item) => item.id === sentMessage.id) ? current : [...current, sentMessage])
			setMessage('')
			setMessageState('idle')
			await loadMessages()
			await loadReview()
		} catch {
			setMessageState('error')
		}
	}, [id, loadMessages, loadReview, message])

	const reject = useCallback(async () => {
		setRejectState('rejecting')
		try {
			await closeOwnerEnquiry(id, '')
			await Promise.all([loadReview(), loadMessages()])
			setRejectState('rejected')
		} catch {
			setRejectState('error')
		}
	}, [id, loadMessages, loadReview])

	return { approvalState, approve, isClosed: review ? ['CLOSED_BY_OWNER', 'CLOSED_BY_CUSTOMER', 'CANCELLED_BY_CUSTOMER'].includes(review.status) : false, loadReview, message, messageState, messages, reject, rejectState, review, sendMessage, setMessage, state }
}
