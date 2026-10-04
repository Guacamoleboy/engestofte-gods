// Pathing
// _______
// src/features/owner-requests-page/OwnerRequestsPage.tsx

import { Link } from 'react-router-dom'
import PageContainer from '../../shared/components/PageContainer'
import { useTranslate } from '../../shared/hooks/useTranslate'
import { useOwnerEnquiries } from './OwnerRequestsPage.hooks'
import styles from './OwnerRequestsPage.module.css'

export default function OwnerRequestsPage() {
	const { content, language } = useTranslate()
	const copy = content.ownerReview
	const { enquiries, loadEnquiries, state } = useOwnerEnquiries()
	const statusLabels = {
		SUBMITTED: copy.statusSubmitted,
		UNDER_REVIEW: copy.statusUnderReview,
		AWAITING_CUSTOMER: copy.statusAwaitingCustomer,
		FOLLOW_UP_REQUIRED: copy.statusFollowUpRequired,
		OWNER_FOLLOW_UP_REQUIRED: copy.statusOwnerFollowUpRequired,
		APPROVED: copy.statusApproved,
	}

	return (
		<main className={styles.page}>
			<PageContainer className={styles.content}>
				<header className={styles.header}>
					<h1>{copy.title}</h1>
					<p>{copy.description}</p>
				</header>

				{state === 'loading' && <p className={styles.state} role="status">{copy.loading}</p>}
				{state === 'error' && (
					<section className={styles.state} role="alert">
						<p>{copy.loadError}</p>
						<button className={styles.action} type="button" onClick={() => void loadEnquiries()}>{copy.retry}</button>
					</section>
				)}
				{state === 'loaded' && enquiries.length === 0 && (
					<section className={styles.state}>
						<h2>{copy.emptyTitle}</h2>
						<p>{copy.emptyDescription}</p>
					</section>
				)}
				{state === 'loaded' && enquiries.length > 0 && (
					<section className={styles.list} aria-label={copy.title}>
						{enquiries.map((enquiry) => (
							<Link className={styles.card} key={enquiry.id} to={`/owner/requests/${enquiry.id}`}>
								<div className={styles.cardContent}>
									<p className={styles.status}>{statusLabels[enquiry.status]}</p>
									<p className={styles.eyebrow}>{copy.customer}</p>
									<h2>{enquiry.customer_name || copy.requestTitle}</h2>
									<p className={styles.requestType}>{copy.requestTitle}</p>
									<p className={styles.submittedAt}>
										<span>{copy.submittedAt}</span>
										<time dateTime={enquiry.submitted_at}>{new Intl.DateTimeFormat(language, { dateStyle: 'long', timeStyle: 'short' }).format(new Date(enquiry.submitted_at))}</time>
									</p>
								</div>
								<span className={styles.cardIndicator} aria-hidden="true">→</span>
							</Link>
						))}
					</section>
				)}
			</PageContainer>
		</main>
	)
}
