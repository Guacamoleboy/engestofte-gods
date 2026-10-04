// Pathing
// _______
// src/features/owner-requests-page/OwnerRequestPage.hooks.ts

import { useCallback, useEffect, useState, type FormEvent } from 'react'
import { useParams } from 'react-router-dom'
import { approveOwnerEnquiry, closeOwnerEnquiry, getOwnerEnquiry, getOwnerEnquiryMessages, saveOwnerEnquiryReview, sendOwnerEnquiryMessage, type EventApproval, type EventMessage, type OwnerEnquiryReview } from '../../api/endpoints/ownerEnquiries'

export function useOwnerEnquiryReview() {
	const { id: rawId } = useParams()
	const id = Number(rawId)
	const [review, setReview] = useState<OwnerEnquiryReview | null>(null)
	const [internalNote, setInternalNote] = useState('')
	const [customerQuestion, setCustomerQuestion] = useState('')
	const [customerNote, setCustomerNote] = useState('')
	const [messages, setMessages] = useState<EventMessage[]>([])
	const [message, setMessage] = useState('')
	const [closeReason, setCloseReason] = useState('')
	const [state, setState] = useState<'loading' | 'loaded' | 'error'>('loading')
	const [saveState, setSaveState] = useState<'idle' | 'saving' | 'saved' | 'error'>('idle')
	const [approval, setApproval] = useState<EventApproval | null>(null)
	const [approvalState, setApprovalState] = useState<'idle' | 'approving' | 'error'>('idle')
	const [messageState, setMessageState] = useState<'idle' | 'sending' | 'error'>('idle')
	const [closeState, setCloseState] = useState<'idle' | 'closing' | 'closed' | 'error'>('idle')

	const loadReview = useCallback(async () => {
		if (!Number.isInteger(id) || id < 1) {
			setState('error')
			return
		}
		setState('loading')
		try {
			const result = await getOwnerEnquiry(id)
			setReview(result)
			setInternalNote(result.internal_note ?? '')
			setCustomerQuestion(result.customer_question ?? '')
			setState('loaded')
		} catch {
			setState('error')
		}
	}, [id])

	const loadMessages = useCallback(async () => {
		try {
			setMessages(await getOwnerEnquiryMessages(id))
		} catch {
			setMessages([])
		}
	}, [id])

	useEffect(() => { void loadReview() }, [loadReview])
	useEffect(() => { void loadMessages() }, [loadMessages])

	const saveReview = useCallback(async (event: FormEvent<HTMLFormElement>) => {
		event.preventDefault()
		setSaveState('saving')
		try {
			const updatedReview = await saveOwnerEnquiryReview(id, internalNote, customerQuestion)
			setReview(updatedReview)
			setInternalNote(updatedReview.internal_note ?? '')
			setCustomerQuestion(updatedReview.customer_question ?? '')
			setSaveState('saved')
		} catch {
			setSaveState('error')
		}
	}, [customerQuestion, id, internalNote])

	const approve = useCallback(async () => {
		setApprovalState('approving')
		try {
			setApproval(await approveOwnerEnquiry(id, customerNote))
			setApprovalState('idle')
			await loadReview()
		} catch {
			setApprovalState('error')
		}
	}, [customerNote, id, loadReview])

	const sendMessage = useCallback(async (event: FormEvent<HTMLFormElement>) => {
		event.preventDefault()
		if (!message.trim()) return
		setMessageState('sending')
		try {
			const sentMessage = await sendOwnerEnquiryMessage(id, message)
			setMessages((current) => [...current, sentMessage])
			setMessage('')
			setCloseState('idle')
			setMessageState('idle')
			await loadReview()
		} catch {
			setMessageState('error')
		}
	}, [id, loadReview, message])

	const closeRequest = useCallback(async (event: FormEvent<HTMLFormElement>) => {
		event.preventDefault()
		if (!closeReason.trim()) return
		setCloseState('closing')
		try {
			await closeOwnerEnquiry(id, closeReason)
			await Promise.all([loadReview(), loadMessages()])
			setCloseState('closed')
		} catch {
			setCloseState('error')
		}
	}, [closeReason, id, loadMessages, loadReview])

	return { approval, approvalState, approve, closeReason, closeRequest, closeState, customerNote, customerQuestion, internalNote, loadReview, loadMessages, message, messageState, messages, review, saveReview, saveState, sendMessage, setCloseReason, setCustomerNote, setCustomerQuestion, setInternalNote, setMessage, state }
}
