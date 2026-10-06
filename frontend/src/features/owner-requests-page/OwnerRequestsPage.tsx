// Pathing
// _______
// src/features/owner-requests-page/OwnerRequestsPage.tsx

import { Link, useLocation } from 'react-router-dom'
import PageContainer from '../../shared/components/PageContainer'
import { useTranslate } from '../../shared/hooks/useTranslate'
import { useOwnerEnquiries } from './OwnerRequestsPage.hooks'
import styles from './OwnerRequestsPage.module.css'

export default function OwnerRequestsPage() {
	const { content, language } = useTranslate()
	const copy = content.ownerReview
	const isBookingsPage = useLocation().pathname === '/owner/bookings'
	const { enquiries, importantMessages, loadEnquiries, state } = useOwnerEnquiries()
	const visibleEnquiries = enquiries.filter((enquiry) => isBookingsPage
		? enquiry.status === 'BOOKED'
		: enquiry.status !== 'BOOKED')
	const visibleEventIds = new Set(visibleEnquiries.flatMap((enquiry) => enquiry.event_id === null ? [] : [enquiry.event_id]))
	const visibleImportantMessages = importantMessages.filter((item) => visibleEventIds.has(item.event_id))
	const statusLabels = {
		SUBMITTED: copy.statusSubmitted,
		UNDER_REVIEW: copy.statusUnderReview,
		AWAITING_CUSTOMER: copy.statusAwaitingCustomer,
		FOLLOW_UP_REQUIRED: copy.statusFollowUpRequired,
		OWNER_FOLLOW_UP_REQUIRED: copy.statusOwnerFollowUpRequired,
		APPROVED: copy.statusApproved,
		AWAITING_DEPOSIT: content.eventsDashboard.statusAwaitingDeposit,
		BOOKED: content.eventsDashboard.statusBooked,
	}

	return (
		<main className={styles.page}>
			<PageContainer className={styles.content}>
				<header className={styles.header}>
					<h1>{isBookingsPage ? copy.bookingsTitle : copy.title}</h1>
					<p>{isBookingsPage ? copy.bookingsDescription : copy.description}</p>
				</header>

				{state === 'loading' && <p className={styles.state} role="status">{copy.loading}</p>}
				{state === 'error' && (
					<section className={styles.state} role="alert">
						<p>{copy.loadError}</p>
						<button className={styles.action} type="button" onClick={() => void loadEnquiries()}>{copy.retry}</button>
					</section>
				)}
				{state === 'loaded' && visibleEnquiries.length === 0 && (
					<section className={styles.state}>
						<h2>{isBookingsPage ? copy.bookingsEmptyTitle : copy.emptyTitle}</h2>
						<p>{isBookingsPage ? copy.bookingsEmptyDescription : copy.emptyDescription}</p>
					</section>
				)}
				{state === 'loaded' && visibleImportantMessages.length > 0 && (
					<section className={styles.list} aria-label={copy.importantMessagesTitle}>
						<h2>{copy.importantMessagesTitle}</h2>
						{visibleImportantMessages.map((item) => <Link className={styles.card} key={item.event_id} to={`/owner/events/${item.event_id}`}>
							<div className={styles.cardContent}>
								<p className={styles.status}>{copy.importantMessage}</p>
								<h2>{item.event_name}</h2>
								<p>{copy.unreadCount.replace('{count}', String(item.unread_count))}</p>
								<p>{item.latest_message}</p>
							</div>
							<span className={styles.cardIndicator} aria-hidden="true">â†’</span>
						</Link>)}
					</section>
				)}
				{state === 'loaded' && visibleEnquiries.length > 0 && (
					<section className={styles.list} aria-label={isBookingsPage ? copy.bookingsTitle : copy.title}>
						{visibleEnquiries.map((enquiry) => (
							<Link className={styles.card} key={enquiry.id} to={enquiry.event_approved_at && enquiry.event_id ? `/owner/events/${enquiry.event_id}` : `/owner/requests/${enquiry.id}`}>
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
