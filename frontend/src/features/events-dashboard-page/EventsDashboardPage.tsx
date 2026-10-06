// Pathing
// _______
// src/features/events-dashboard-page/EventsDashboardPage.tsx

import { Link, useLocation } from 'react-router-dom'
import type { EnquirySummary } from '../../api/endpoints/myEnquiries'
import PageContainer from '../../shared/components/PageContainer'
import { useTranslate } from '../../shared/hooks/useTranslate'
import { useEventsDashboard } from './EventsDashboardPage.hooks'
import styles from './EventsDashboardPage.module.css'

export default function EventsDashboardPage() {
	const { content, language } = useTranslate()
	const copy = content.eventsDashboard
	const isBookingsPage = useLocation().pathname === '/dashboard/bookings'
	const { enquiries, importantMessages, loadEnquiries, state } = useEventsDashboard()
	const visibleEnquiries = enquiries.filter((enquiry) => isBookingsPage
		? enquiry.status === 'BOOKED'
		: enquiry.status !== 'BOOKED')
	const visibleEventIds = new Set(visibleEnquiries.flatMap((enquiry) => enquiry.event_id === null ? [] : [enquiry.event_id]))
	const visibleImportantMessages = importantMessages.filter((item) => visibleEventIds.has(item.event_id))

	return (
		<div className={styles.dashboard}>
			<PageContainer className={styles.content}>
				<header className={styles.header}>
					<h1>{isBookingsPage ? copy.bookingsTitle : copy.title}</h1>
					<p>{isBookingsPage ? copy.bookingsDescription : copy.description}</p>
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
				{state === 'loaded' && visibleEnquiries.length === 0 && (
					<section className={styles.emptyState}>
						<h2>{isBookingsPage ? copy.bookingsEmptyTitle : copy.emptyTitle}</h2>
						<p>{isBookingsPage ? copy.bookingsEmptyDescription : copy.emptyDescription}</p>
					</section>
				)}
				{state === 'loaded' && visibleImportantMessages.length > 0 && (
					<section className={styles.list} aria-label={copy.importantMessagesTitle}>
						<h2>{copy.importantMessagesTitle}</h2>
						{visibleImportantMessages.map((item) => <Link className={styles.card} key={item.event_id} to={`/dashboard/events/${item.event_id}`}>
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
							<EnquiryCard key={enquiry.submission_id} enquiry={enquiry} language={language} copy={copy} />
						))}
					</section>
				)}
			</PageContainer>
		</div>
	)
}

function EnquiryCard({ enquiry, language, copy }: { enquiry: EnquirySummary; language: string; copy: ReturnType<typeof useTranslate>['content']['eventsDashboard'] }) {
	const isClickable = true
	const cardContent = (
		<>
			<div className={styles.cardContent}>
				<p className={`${styles.status} ${isClickable ? styles.approvedStatus : ''}`}>{statusLabelsFor(enquiry.status, copy)}</p>
				<h2>{copy.requestTitle}</h2>
				<p className={styles.submittedAt}>
					<span>{copy.submittedAt}</span>
					<time dateTime={enquiry.submitted_at}>{new Intl.DateTimeFormat(language, { dateStyle: 'long', timeStyle: 'short' }).format(new Date(enquiry.submitted_at))}</time>
				</p>
				{enquiry.status === 'FOLLOW_UP_REQUIRED' && <p className={styles.customerQuestion}>{copy.ownerReplied}</p>}
			</div>
			{isClickable && <span className={styles.cardIndicator} aria-hidden="true">→</span>}
		</>
	)

	return isClickable
		? <Link className={`${styles.card} ${styles.clickableCard}`} to={enquiry.event_id && ['APPROVED', 'AWAITING_DEPOSIT', 'BOOKED', 'CANCELLED_BY_CUSTOMER', 'CLOSED_BY_OWNER', 'CLOSED_BY_CUSTOMER'].includes(enquiry.status) ? `/dashboard/events/${enquiry.event_id}` : `/dashboard/approval/${enquiry.submission_id}`}>{cardContent}</Link>
		: <article className={styles.card}>{cardContent}</article>
}

function statusLabelsFor(status: EnquirySummary['status'], copy: ReturnType<typeof useTranslate>['content']['eventsDashboard']) {
	return {
		SUBMITTED: copy.statusSubmitted,
		UNDER_REVIEW: copy.statusUnderReview,
		AWAITING_CUSTOMER: copy.statusAwaitingCustomer,
		APPROVED: copy.statusApproved,
		AWAITING_DEPOSIT: copy.statusAwaitingDeposit,
		BOOKED: copy.statusBooked,
		CANCELLED_BY_CUSTOMER: copy.statusCancelled,
		FOLLOW_UP_REQUIRED: copy.statusFollowUpRequired,
		OWNER_FOLLOW_UP_REQUIRED: copy.statusOwnerFollowUpRequired,
		CLOSED_BY_OWNER: copy.statusClosed,
		CLOSED_BY_CUSTOMER: copy.statusClosed,
	}[status]
}
