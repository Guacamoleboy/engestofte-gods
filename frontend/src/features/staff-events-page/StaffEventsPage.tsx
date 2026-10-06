// Pathing
// _______
// src/features/staff-events-page/StaffEventsPage.tsx

import { Link } from 'react-router-dom'
import PageContainer from '../../shared/components/PageContainer'
import { useTranslate } from '../../shared/hooks/useTranslate'
import { useStaffEvents } from './StaffEventsPage.hooks'
import styles from './StaffEventsPage.module.css'

export default function StaffEventsPage() {
	const { content } = useTranslate()
	const copy = content.eventsDashboard
	const { events, importantMessages, loadEvents, state } = useStaffEvents()

	return (
		<main className={styles.page}>
			<PageContainer className={styles.content}>
				<header className={styles.header}>
					<h1>{copy.staffTitle}</h1>
					<p>{copy.staffDescription}</p>
				</header>

				{state === 'loading' && <p className={styles.state} role="status">{copy.loading}</p>}
				{state === 'error' && <section className={styles.state} role="alert">
					<p>{copy.error}</p>
					<button type="button" onClick={() => void loadEvents()}>{copy.retry}</button>
				</section>}
				{state === 'loaded' && events.length === 0 && <section className={styles.emptyState}>
					<h2>{copy.staffEmptyTitle}</h2>
					<p>{copy.staffEmptyDescription}</p>
				</section>}
				{state === 'loaded' && importantMessages.length > 0 && <section className={styles.list} aria-label={copy.importantMessagesTitle}>
					<h2>{copy.importantMessagesTitle}</h2>
					{importantMessages.map((item) => <Link className={styles.card} key={item.event_id} to={`/staff/events/${item.event_id}`}>
						<div className={styles.cardContent}>
							<p className={styles.status}>{copy.importantMessage}</p>
							<h2>{item.event_name}</h2>
							<p>{copy.unreadCount.replace('{count}', String(item.unread_count))}</p>
							<p>{item.latest_message}</p>
						</div>
						<span className={styles.cardIndicator} aria-hidden="true">â†’</span>
					</Link>)}
				</section>}
				{state === 'loaded' && events.length > 0 && <section className={styles.list} aria-label={copy.staffTitle}>
					{events.map((event) => <Link className={styles.card} key={event.event_id} to={`/staff/events/${event.event_id}`}>
						<div className={styles.cardContent}>
							<p className={`${styles.status} ${event.status === 'APPROVED' ? styles.approvedStatus : ''}`}>{eventStatusLabel(event.status, content.ownerEvent)}</p>
							<h2>{event.event_name || copy.eventTitleDefault}</h2>
							{event.customer_name && <p>{content.ownerEvent.customerName}: {event.customer_name}</p>}
							<p>{copy.eventGuestCount}: {event.expected_guest_count ?? '—'}</p>
							<p className={styles.submittedAt}><span>{copy.eventRequestedDate}</span>{event.requested_date || '—'}</p>
						</div>
						<span className={styles.cardIndicator} aria-hidden="true">→</span>
					</Link>)}
				</section>}
			</PageContainer>
		</main>
	)
}

function eventStatusLabel(status: string, copy: ReturnType<typeof useTranslate>['content']['ownerEvent']) {
	if (status === 'AWAITING_APPROVAL') return copy.statusAwaitingApproval
	if (status === 'AWAITING_DEPOSIT') return copy.statusAwaitingDeposit
	if (status === 'BOOKED') return copy.statusBooked
	if (status === 'CLOSED_BY_CUSTOMER' || status === 'CLOSED_BY_OWNER' || status === 'CANCELLED_BY_CUSTOMER') return copy.statusClosed
	return copy.statusApproved
}
