// Pathing
// _______
// src/features/owner-requests-page/OwnerRequestPage.tsx

import { useState } from 'react'
import { Link, Navigate } from 'react-router-dom'
import PageContainer from '../../shared/components/PageContainer'
import { useTranslate } from '../../shared/hooks/useTranslate'
import { useOwnerEnquiryReview } from './OwnerRequestPage.hooks'
import styles from './OwnerRequestsPage.module.css'

export default function OwnerRequestPage() {
	const { content, language } = useTranslate()
	const copy = content.ownerReview
	const { approvalState, approve, isClosed, loadReview, message, messageState, messages, reject, rejectState, review, sendMessage, setMessage, state } = useOwnerEnquiryReview()
	const [activeTab, setActiveTab] = useState<'ai' | 'status' | 'request' | 'messages'>('status')

	if (state === 'loading') return <main className={styles.page}><PageContainer className={styles.content}><p role="status">{copy.loading}</p></PageContainer></main>
	if (state === 'error' || !review) {
		return <main className={styles.page}><PageContainer className={styles.content}><section className={styles.state} role="alert"><p>{copy.loadError}</p><button className={styles.action} type="button" onClick={() => void loadReview()}>{copy.retry}</button></section></PageContainer></main>
	}
	if (review.event_approved_at && review.event_id) return <Navigate to={`/owner/events/${review.event_id}`} replace />

	const assessment = review.ai_assessment
	const transcript = review.draft.conversation ?? []
	const statusLabels = {
		SUBMITTED: copy.statusSubmitted,
		UNDER_REVIEW: copy.statusUnderReview,
		AWAITING_CUSTOMER: copy.statusAwaitingCustomer,
		FOLLOW_UP_REQUIRED: copy.statusFollowUpRequired,
		OWNER_FOLLOW_UP_REQUIRED: copy.statusOwnerFollowUpRequired,
		APPROVED: copy.statusApproved,
		CLOSED_BY_OWNER: copy.statusClosed,
		CLOSED_BY_CUSTOMER: copy.statusClosed,
		CANCELLED_BY_CUSTOMER: copy.statusClosed,
	}

	return (
		<main className={styles.page}>
			<PageContainer className={styles.content}>
				<Link className={styles.backLink} to="/owner/requests">← {copy.backToRequests}</Link>
				<header className={styles.header}>
					<p className={styles.eyebrow}>{copy.requestTitle} · {statusLabels[review.status]}</p>
					<h1>{review.draft.customerName || copy.requestTitle}</h1>
					<p className={styles.submittedAt}><span>{copy.submittedAt}</span><time dateTime={review.submitted_at}>{new Intl.DateTimeFormat(language, { dateStyle: 'long', timeStyle: 'short' }).format(new Date(review.submitted_at))}</time></p>
					{review.draft.expectedGuestCount != null && <p>{copy.guestCount}: {review.draft.expectedGuestCount}</p>}
				</header>

				<section className={styles.panel}>
					<nav className={styles.tabs} aria-label={copy.requestTitle}>
						<button type="button" className={activeTab === 'ai' ? styles.activeTab : styles.tab} aria-pressed={activeTab === 'ai'} onClick={() => setActiveTab('ai')}>{copy.aiTab}</button>
						<button type="button" className={activeTab === 'status' ? styles.activeTab : styles.tab} aria-pressed={activeTab === 'status'} onClick={() => setActiveTab('status')}>{copy.statusTab}</button>
						<button type="button" className={activeTab === 'request' ? styles.activeTab : styles.tab} aria-pressed={activeTab === 'request'} onClick={() => setActiveTab('request')}>{copy.requestTab}</button>
						<button type="button" className={activeTab === 'messages' ? styles.activeTab : styles.tab} aria-pressed={activeTab === 'messages'} onClick={() => setActiveTab('messages')}>{copy.messagesTab}</button>
					</nav>
					{activeTab === 'ai' ? <section className={styles.assessmentPanel}>
						<h2>{copy.assessmentTitle}</h2>
						{assessment ? <><p className={styles.summary}>{assessment.summary}</p><AssessmentList title={copy.missingInformation} items={assessment.missing_information} /><AssessmentList title={copy.uncertainties} items={assessment.uncertainties} /><AssessmentList title={copy.conflicts} items={assessment.conflicts} /><AssessmentList title={copy.upsellSuggestions} items={assessment.upsell_suggestions} /></> : <p>{copy.assessmentUnavailable}</p>}
					</section> : activeTab === 'request' ? <section className={`${styles.assessmentPanel} ${styles.requestPanel}`}>
						<h2>{copy.requestTab}</h2>
						{transcript.map((turn, index) => <div className={styles.transcriptPair} key={`${index}-${turn.question}`}><article className={`${styles.message} ${styles.aiMessage}`}><p className={styles.messageMeta}><strong>{copy.aiFlowLabel}</strong></p><p>{turn.question}</p></article><article className={`${styles.message} ${styles.customerMessage}`}><p className={styles.messageMeta}><strong>{copy.customerLabel}</strong></p><p>{turn.answer}</p></article></div>)}
						{transcript.length === 0 && <p>{copy.emptyThread}</p>}
					</section> : activeTab === 'status' ? <section className={`${styles.panel} ${styles.approvalPanel}`}>
						<h2>{copy.approveTitle}</h2>
						<p>{copy.approvalDescription}</p>
						{approvalState === 'error' && <p className={styles.error} role="alert">{copy.approvalError}</p>}
						<div className={styles.actions}>
							<button className={styles.action} type="button" disabled={approvalState === 'approving' || isClosed} onClick={() => void approve()}>{approvalState === 'approving' ? copy.approving : copy.approveAction}</button>
							<button className={styles.declineAction} type="button" disabled={rejectState === 'rejecting' || isClosed} onClick={() => { if (window.confirm(copy.declineConfirm)) void reject() }}>{rejectState === 'rejecting' ? copy.declining : copy.declineAction}</button>
						</div>
						{rejectState === 'error' && <p className={styles.error} role="alert">{copy.declineError}</p>}
						{rejectState === 'rejected' && <p className={styles.success} role="status">{copy.declinedNotice}</p>}
					</section> : <section className={`${styles.messagePanel}`}>
						<h2>{copy.threadTitle}</h2>
						<div className={styles.messages} aria-live="polite">
							{messages.map((item) => (
								<article className={`${styles.message} ${item.sender_type === 'OWNER' ? styles.ownerMessage : styles.customerMessage}`} key={item.id}>
									<p className={styles.messageMeta}><strong>{item.sender_type === 'OWNER' ? copy.ownerLabel : copy.customerLabel}</strong><time dateTime={item.created_at}>{new Intl.DateTimeFormat(language, { dateStyle: 'medium', timeStyle: 'short' }).format(new Date(item.created_at))}</time></p>
									<p>{item.content}</p>
								</article>
							))}
							{messages.length === 0 && <p>{copy.emptyThread}</p>}
						</div>
						{!isClosed && <form className={styles.messageComposer} onSubmit={sendMessage}>
							<label htmlFor="owner-message">{copy.followUpQuestion}</label>
							<textarea id="owner-message" value={message} maxLength={5000} onChange={(event) => setMessage(event.currentTarget.value)} />
							{messageState === 'error' && <p className={styles.error} role="alert">{copy.messageError}</p>}
							<button className={styles.action} type="submit" disabled={messageState === 'sending' || !message.trim()}>{messageState === 'sending' ? copy.messageSending : copy.sendFollowUp}</button>
						</form>}
					</section>}
				</section>
			</PageContainer>
		</main>
	)
}

function AssessmentList({ title, items }: { title: string; items: string[] }) {
	const { content } = useTranslate()
	return (
		<section className={styles.assessmentSection}>
			<h3>{title}</h3>
			{items.length > 0
				? <ul>{items.map((item, index) => <li key={`${index}-${item}`}>{item}</li>)}</ul>
				: <p>{content.ownerReview.noItems}</p>}
		</section>
	)
}
