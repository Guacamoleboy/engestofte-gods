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
	const { customerQuestion, internalNote, loadReview, review, saveReview, saveState, setCustomerQuestion, setInternalNote, state } = useOwnerEnquiryReview()

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
								<textarea id="internal-note" value={internalNote} maxLength={10000} onChange={(event) => setInternalNote(event.currentTarget.value)} />
							</section>
							<section className={styles.formSection}>
								<h2>{copy.customerQuestionTitle}</h2>
								<p>{copy.customerQuestionDescription}</p>
								<label htmlFor="customer-question">{copy.customerQuestionLabel}</label>
								<textarea id="customer-question" value={customerQuestion} maxLength={1500} onChange={(event) => setCustomerQuestion(event.currentTarget.value)} />
							</section>
							{saveState === 'error' && <p className={styles.error} role="alert">{copy.saveError}</p>}
							{saveState === 'saved' && <p className={styles.success} role="status">{copy.saved}</p>}
							<button className={styles.action} type="submit" disabled={saveState === 'saving'}>{saveState === 'saving' ? copy.saving : copy.save}</button>
						</form>
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
