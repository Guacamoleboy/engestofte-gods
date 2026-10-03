// Pathing
// _______
// src/features/ai-flow-redirect-page/AiFlowRedirectPage.tsx

import { Link, useLocation, useNavigate } from 'react-router-dom'
import { useTranslate } from '../../shared/hooks/useTranslate'
import { AI_FLOW_DRAFT, getAiFlowDraftStatus } from '../../shared/data/aiFlowDraft'
import styles from './AiFlowRedirectPage.module.css'

export default function AiFlowRedirectPage() {
	const { content } = useTranslate()
	const location = useLocation()
	const navigate = useNavigate()
	const copy = content.aiFlow.redirect
	const customerName = typeof location.state?.customerName === 'string' ? location.state.customerName : ''
	const storedDraftStatus = getAiFlowDraftStatus()
	const draftNotice = location.state?.draftNotice ?? (storedDraftStatus === 'none' ? null : storedDraftStatus)
	const isDraftResume = draftNotice === 'saved' || draftNotice === 'complete' || draftNotice === 'unreadable'
	const title = copy.title.replace('{name}, ', customerName ? `${customerName}, ` : '')
	const handoffState = { customerName, enquiryDraft: { draftKey: AI_FLOW_DRAFT.key, draftVersion: AI_FLOW_DRAFT.version } }

	function continueDraft() {
		navigate('/ai-flow', { state: { resumeDraft: true } })
	}

	function startNewDraft() {
		if (!window.confirm(content.aiFlow.confirmNewDraft)) return
		window.localStorage.removeItem(AI_FLOW_DRAFT.key)
		navigate('/ai-flow', { replace: true, state: { startFresh: true } })
	}

	return (
		<main className={styles.page}>
			{isDraftResume && (
				<aside className={styles.draftToast} role="status">
					<p>{draftNotice === 'unreadable'
						? content.aiFlow.draftUnreadableNotice
						: draftNotice === 'complete'
							? content.aiFlow.draftCompleteNotice
							: <>{content.aiFlow.draftResumeNotice} {content.aiFlow.draftStorageNotice}</>}</p>
					<div className={styles.toastActions}>
						{draftNotice === 'saved' && <button type="button" onClick={continueDraft}>{content.aiFlow.continueDraft}</button>}
						<button type="button" onClick={startNewDraft}>{content.aiFlow.startNewDraft}</button>
					</div>
				</aside>
			)}
			<div className={styles.content}>
				<img className={styles.logo} src="/images/shared/logo-white.png" alt="Engestofte Gods" />
				<h1>{title}</h1>
				<p>{copy.description}</p>
				<p>{copy.contactExplanation}</p>
				<div className={styles.actions}>
					<Link className={styles.createAccount} to="/register" state={handoffState}>{copy.createAccount}</Link>
					<Link className={styles.sendEmail} to="/login" state={handoffState}>{copy.login}</Link>
					<Link className={styles.cancelEnquiry} to="/kontakt">{copy.cancelEnquiry}</Link>
				</div>
			</div>
		</main>
	)
}
