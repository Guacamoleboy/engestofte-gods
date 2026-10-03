// Pathing
// _______
// src/features/events-dashboard-page/EventsDashboardPage.tsx

import { Link } from 'react-router-dom'
import PageContainer from '../../shared/components/PageContainer'
import { useTranslate } from '../../shared/hooks/useTranslate'
import DashboardNavigation from './DashboardNavigation'
import { useEventsDashboard } from './EventsDashboardPage.hooks'
import styles from './EventsDashboardPage.module.css'

export default function EventsDashboardPage() {
	const { content, language } = useTranslate()
	const copy = content.eventsDashboard
	const { enquiries, loadEnquiries, state } = useEventsDashboard()
	const statusLabels = {
		SUBMITTED: copy.statusSubmitted,
		UNDER_REVIEW: copy.statusUnderReview,
		AWAITING_CUSTOMER: copy.statusAwaitingCustomer,
		APPROVED: copy.statusApproved,
		CANCELLED_BY_CUSTOMER: copy.statusCancelled,
	}

	return (
		<main className={styles.dashboard}>
			<DashboardNavigation />
			<PageContainer className={styles.content}>
				<header className={styles.header}>
					<h1>{copy.title}</h1>
					<p>{copy.description}</p>
				</header>

				{state === 'loading' && <p className={styles.state} role="status">{copy.loading}</p>}
				{state === 'unauthenticated' && (
					<section className={styles.state}>
						<p>{copy.unauthenticated}</p>
						<Link className={styles.action} to="/login">{copy.login}</Link>
					</section>
				)}
				{state === 'error' && (
					<section className={styles.state} role="alert">
						<p>{copy.error}</p>
						<button className={styles.action} type="button" onClick={() => void loadEnquiries()}>{copy.retry}</button>
					</section>
				)}
				{state === 'loaded' && enquiries.length === 0 && (
					<section className={styles.emptyState}>
						<h2>{copy.emptyTitle}</h2>
						<p>{copy.emptyDescription}</p>
					</section>
				)}
				{state === 'loaded' && enquiries.length > 0 && (
					<section className={styles.list} aria-label={copy.title}>
						{enquiries.map((enquiry) => (
							<article className={styles.card} key={enquiry.submission_id}>
								<div>
									<h2>{copy.requestTitle}</h2>
									<p className={styles.submittedAt}>
										<span>{copy.submittedAt}</span>
										<time dateTime={enquiry.submitted_at}>{new Intl.DateTimeFormat(language, { dateStyle: 'long', timeStyle: 'short' }).format(new Date(enquiry.submitted_at))}</time>
									</p>
								</div>
								<p className={styles.status}>
									{statusLabels[enquiry.status]}
								</p>
							</article>
						))}
					</section>
				)}
			</PageContainer>
		</main>
	)
}
