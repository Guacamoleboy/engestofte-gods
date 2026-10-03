// Pathing
// _______
// src/features/auth-page/AuthPage.hooks.ts

import { useState, type FormEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import { ApiError } from '../../api/client'
import { login, register } from '../../api/endpoints/auth'
import { submitEnquiry } from '../../api/endpoints/enquiries'
import { clearAiFlowDraft, getAiFlowDraftStatus, getCompleteAiFlowDraft, getOrCreateSubmissionId } from '../../shared/data/aiFlowDraft'

const PENDING_SUBMISSION_KEY = 'engestofte.pendingSubmissionId'

export function useAuthPage(mode: 'login' | 'register') {
	const navigate = useNavigate()
	const draft = getCompleteAiFlowDraft()
	const [email, setEmail] = useState('')
	const [password, setPassword] = useState('')
	const [fullName, setFullName] = useState(typeof draft?.customerName === 'string' ? draft.customerName : '')
	const [error, setError] = useState('')
	const [isSubmitting, setIsSubmitting] = useState(false)

	async function submit(event: FormEvent<HTMLFormElement>) {
		event.preventDefault()
		if (isSubmitting) return
		setError('')
		setIsSubmitting(true)

		try {
			const draftStatus = getAiFlowDraftStatus()
			const completedDraft = getCompleteAiFlowDraft()
			const submissionId = draftStatus === 'complete' && completedDraft ? getOrCreateSubmissionId() : null
			const hasRetryableSession = submissionId !== null
				&& window.localStorage.getItem(PENDING_SUBMISSION_KEY) === submissionId
				&& Boolean(window.localStorage.getItem('access_token'))

			if (!hasRetryableSession) {
				const auth = mode === 'register'
					? await register(fullName.trim(), email.trim(), password)
					: await login(email.trim(), password)
				window.localStorage.setItem('access_token', auth.access_token)
				window.localStorage.setItem('refresh_token', auth.refresh_token)
				if (submissionId) window.localStorage.setItem(PENDING_SUBMISSION_KEY, submissionId)
			}

			if (submissionId && completedDraft) {
				await submitEnquiry({
					submission_id: submissionId,
					language: String(completedDraft.language),
					draft: getCompleteAiFlowDraft() ?? completedDraft,
				})
				clearAiFlowDraft()
				window.localStorage.removeItem(PENDING_SUBMISSION_KEY)
			}

			navigate('/dashboard/events/', { replace: true })
		} catch (caughtError) {
			if (caughtError instanceof ApiError && caughtError.status === 401) {
				window.localStorage.removeItem('access_token')
				window.localStorage.removeItem('refresh_token')
				window.localStorage.removeItem(PENDING_SUBMISSION_KEY)
			}
			setError(caughtError instanceof Error ? caughtError.message : 'Request failed')
		} finally {
			setIsSubmitting(false)
		}
	}

	return { email, error, fullName, isSubmitting, password, setEmail, setFullName, setPassword, submit }
}
