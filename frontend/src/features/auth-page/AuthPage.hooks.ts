// Pathing
// _______
// src/features/auth-page/AuthPage.hooks.ts

import { useState, type FormEvent } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import { ApiError } from '../../api/client'
import { login, register } from '../../api/endpoints/auth'
import { submitEnquiry } from '../../api/endpoints/enquiries'
import { AI_FLOW_DRAFT, clearAiFlowDraft, getAiFlowDraftStatus, getCompleteAiFlowDraft, getOrCreateSubmissionId } from '../../shared/data/aiFlowDraft'
import { canAccessDashboardPath, getDashboardPath, type AccountRole } from '../../shared/data/authSession'
import { useAuth } from '../../shared/hooks/useAuth'

const PENDING_SUBMISSION_KEY = 'engestofte.pendingSubmissionId'

type AuthLocationState = {
	from?: { pathname?: unknown }
	enquiryDraft?: { draftKey?: unknown; draftVersion?: unknown }
}

export function useAuthPage(mode: 'login' | 'register') {
	const navigate = useNavigate()
	const location = useLocation()
	const { isAuthenticated, setSession, clearSession, user } = useAuth()
	const locationState = location.state as AuthLocationState | null
	const isEnquiryHandoff = locationState?.enquiryDraft?.draftKey === AI_FLOW_DRAFT.key
		&& locationState?.enquiryDraft?.draftVersion === AI_FLOW_DRAFT.version
	const draft = isEnquiryHandoff ? getCompleteAiFlowDraft() : null
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
			const draftStatus = isEnquiryHandoff ? getAiFlowDraftStatus() : 'none'
			const completedDraft = isEnquiryHandoff ? getCompleteAiFlowDraft() : null
			const submissionId = draftStatus === 'complete' && completedDraft ? getOrCreateSubmissionId() : null
			const hasRetryableSession = submissionId !== null
				&& window.localStorage.getItem(PENDING_SUBMISSION_KEY) === submissionId
				&& isAuthenticated

			let role: AccountRole | null = user?.role ?? null
			if (!hasRetryableSession) {
				const auth = mode === 'register'
					? await register(fullName.trim(), email.trim(), password)
					: await login(email.trim(), password)
				setSession(auth)
				role = auth.account.role
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

			if (!role) throw new Error('The signed-in account role is unavailable')
			const requestedPath = locationState?.from?.pathname
			const returnPath = typeof requestedPath === 'string' && canAccessDashboardPath(requestedPath, role)
				? requestedPath
				: getDashboardPath(role)
			navigate(returnPath, { replace: true })
		} catch (caughtError) {
			if (caughtError instanceof ApiError && caughtError.status === 401) {
				clearSession()
			}
			setError(caughtError instanceof Error ? caughtError.message : 'Request failed')
		} finally {
			setIsSubmitting(false)
		}
	}

	return { email, error, fullName, isSubmitting, password, setEmail, setFullName, setPassword, submit }
}
