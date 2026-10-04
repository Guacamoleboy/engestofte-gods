// Pathing
// _______
// src/features/owner-requests-page/OwnerRequestPage.hooks.ts

import { useCallback, useEffect, useState, type FormEvent } from 'react'
import { useParams } from 'react-router-dom'
import { getOwnerEnquiry, saveOwnerEnquiryReview, type OwnerEnquiryReview } from '../../api/endpoints/ownerEnquiries'

export function useOwnerEnquiryReview() {
	const { id: rawId } = useParams()
	const id = Number(rawId)
	const [review, setReview] = useState<OwnerEnquiryReview | null>(null)
	const [internalNote, setInternalNote] = useState('')
	const [customerQuestion, setCustomerQuestion] = useState('')
	const [state, setState] = useState<'loading' | 'loaded' | 'error'>('loading')
	const [saveState, setSaveState] = useState<'idle' | 'saving' | 'saved' | 'error'>('idle')

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

	useEffect(() => {
		void loadReview()
	}, [loadReview])

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

	return { customerQuestion, internalNote, loadReview, review, saveReview, saveState, setCustomerQuestion, setInternalNote, state }
}
