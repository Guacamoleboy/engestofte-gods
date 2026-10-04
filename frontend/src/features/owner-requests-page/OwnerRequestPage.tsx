// Pathing
// _______
// src/features/owner-requests-page/OwnerRequestPage.tsx

import { Link } from 'react-router-dom'
import PageContainer from '../../shared/components/PageContainer'
import { useTranslate } from '../../shared/hooks/useTranslate'
import { useOwnerEnquiryReview } from './OwnerRequestPage.hooks'
import styles from './OwnerRequestsPage.module.css'

export default function OwnerRequestPage() {
	const { content, language } = useTranslate()
	const copy = content.ownerReview
	const { approval, approvalState, approve, closeReason, closeRequest, closeState, customerNote, customerQuestion, internalNote, loadReview, message, messageState, messages, review, saveReview, saveState, sendMessage, setCloseReason, setCustomerNote, setCustomerQuestion, setInternalNote, setMessage, state } = useOwnerEnquiryReview()

	if (state === 'loading') return <main className={styles.page}><PageContainer className={styles.content}><p role="status">{copy.loading}</p></PageContainer></main>
	if (state === 'error' || !review) {
		return <main className={styles.page}><PageContainer className={styles.content}><section className={styles.state} role="alert"><p>{copy.loadError}</p><button className={styles.action} type="button" onClick={() => void loadReview()}>{copy.retry}</button></section></PageContainer></main>
	}

	const assessment = review.ai_assessment
	const conversation = review.draft.conversation ?? []
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
	const hasCompleteAssessment = Boolean(assessment
		&& Array.isArray(assessment.missing_information)
		&& Array.isArray(assessment.conflicts))
	const approvalBlocked = review.status === 'AWAITING_CUSTOMER'
		|| review.status === 'FOLLOW_UP_REQUIRED'
		|| Boolean(review.event_approved_at)
		|| review.status === 'CLOSED_BY_OWNER'
		|| review.status === 'CLOSED_BY_CUSTOMER'
		|| review.status === 'CANCELLED_BY_CUSTOMER'
		|| !review.draft.isComplete
		|| !review.draft.customerName?.trim()
		|| !review.draft.expectedGuestCount
		|| review.draft.expectedGuestCount < 1
		|| review.draft.expectedGuestCount > 150
		|| !conversation.length
		|| !hasCompleteAssessment
		|| Boolean(assessment?.missing_information.length)
		|| Boolean(assessment?.conflicts.length)
	const canApprove = !approvalBlocked && !approval && saveState !== 'saving'
	const isClosed = review.status === 'CLOSED_BY_OWNER' || review.status === 'CLOSED_BY_CUSTOMER' || review.status === 'CANCELLED_BY_CUSTOMER'

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

				<div className={styles.reviewGrid}>
					<div className={styles.reviewContent}>
						<section className={styles.panel}>
							<h2>{copy.assessmentTitle}</h2>
							{assessment ? (
								<>
									<p className={styles.summary}>{assessment.summary}</p>
									<AssessmentList title={copy.missingInformation} items={assessment.missing_information} />
									<AssessmentList title={copy.uncertainties} items={assessment.uncertainties} />
									<AssessmentList title={copy.conflicts} items={assessment.conflicts} />
									<AssessmentList title={copy.upsellSuggestions} items={assessment.upsell_suggestions} />
								</>
							) : <p>{copy.assessmentUnavailable}</p>}
						</section>

						<section className={styles.panel}>
							<h2>{copy.conversationTitle}</h2>
							{conversation.map((turn, index) => (
								<article className={styles.answer} key={`${index}-${turn.question}`}>
									<h3>{turn.question}</h3>
									<p>{turn.answer}</p>
								</article>
							))}
						</section>
					</div>

					<aside className={styles.reviewSidebar}>
						<form className={styles.panel} onSubmit={saveReview}>
							<section className={styles.formSection}>
								<h2>{copy.internalNoteTitle}</h2>
								<p>{copy.internalNoteDescription}</p>
								<label htmlFor="internal-note">{copy.internalNoteLabel}</label>
								<textarea id="internal-note" value={internalNote} maxLength={10000} disabled={Boolean(approval) || Boolean(review.event_approved_at) || isClosed} onChange={(event) => setInternalNote(event.currentTarget.value)} />
							</section>
							{!review.event_id && <section className={styles.formSection}>
								<h2>{copy.customerQuestionTitle}</h2>
								<p>{copy.customerQuestionDescription}</p>
								<label htmlFor="customer-question">{copy.customerQuestionLabel}</label>
								<textarea id="customer-question" value={customerQuestion} maxLength={1500} disabled={isClosed} onChange={(event) => setCustomerQuestion(event.currentTarget.value)} />
							</section>}
							{saveState === 'error' && <p className={styles.error} role="alert">{copy.saveError}</p>}
							{saveState === 'saved' && <p className={styles.success} role="status">{copy.saved}</p>}
							<button className={styles.action} type="submit" disabled={saveState === 'saving' || Boolean(approval) || Boolean(review.event_approved_at) || isClosed}>{saveState === 'saving' ? copy.saving : copy.save}</button>
						</form>
						<section className={`${styles.panel} ${styles.messagePanel}`}>
							<h2>{copy.threadTitle}</h2>
							<div className={styles.messages} aria-live="polite">
								{messages.map((item) => <article className={styles.message} key={item.id}><p><strong>{item.sender_type === 'OWNER' ? copy.ownerLabel : copy.customerLabel}</strong><time dateTime={item.created_at}>{new Intl.DateTimeFormat(language, { dateStyle: 'medium', timeStyle: 'short' }).format(new Date(item.created_at))}</time></p><div>{item.content}</div></article>)}
								{messages.length === 0 && <p>{copy.emptyThread}</p>}
							</div>
							{!isClosed && <form className={styles.messageComposer} onSubmit={sendMessage}>
								<label htmlFor="owner-message">{copy.messageLabel}</label>
								<textarea id="owner-message" value={message} maxLength={5000} onChange={(event) => setMessage(event.currentTarget.value)} />
								{messageState === 'error' && <p className={styles.error} role="alert">{copy.messageError}</p>}
								<button className={styles.action} type="submit" disabled={messageState === 'sending' || !message.trim()}>{messageState === 'sending' ? copy.messageSending : copy.sendMessage}</button>
							</form>}
							{review.status === 'OWNER_FOLLOW_UP_REQUIRED' && !review.event_approved_at && <form className={styles.closeForm} onSubmit={closeRequest}>
								<h3>{copy.closeTitle}</h3>
								<label htmlFor="close-reason">{copy.closeReasonLabel}</label>
								<textarea id="close-reason" value={closeReason} maxLength={5000} onChange={(event) => setCloseReason(event.currentTarget.value)} />
								{closeState === 'error' && <p className={styles.error} role="alert">{copy.closeError}</p>}
								<button className={styles.action} type="submit" disabled={closeState === 'closing' || !closeReason.trim()}>{closeState === 'closing' ? copy.closing : copy.closeAction}</button>
							</form>}
							{closeState === 'closed' && <p className={styles.success} role="status">{copy.closedNotice}</p>}
						</section>
						<section className={styles.panel}>
							<h2>{copy.approvalTitle}</h2>
							<p>{copy.approvalDescription}</p>
							<label htmlFor="customer-note">{copy.customerNoteLabel}</label>
							<textarea id="customer-note" value={customerNote} maxLength={10000} disabled={Boolean(approval) || isClosed} onChange={(event) => setCustomerNote(event.currentTarget.value)} />
							{approvalBlocked && !approval && !isClosed && !review.event_approved_at && <p>{copy.approvalBlocked}</p>}
							{approvalState === 'error' && <p className={styles.error} role="alert">{copy.approvalError}</p>}
							{approval && <p className={styles.success} role="status">{copy.eventCreated.replace('{id}', String(approval.event_id))}</p>}
							<button className={styles.action} type="button" disabled={!canApprove || approvalState === 'approving' || isClosed} onClick={() => void approve()}>
								{approvalState === 'approving' ? copy.approving : copy.approvalAction}
							</button>
						</section>
					</aside>
				</div>
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
