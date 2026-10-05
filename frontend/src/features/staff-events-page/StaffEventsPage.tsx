// Pathing
// _______
// src/features/staff-events-page/StaffEventsPage.tsx

import { Link } from 'react-router-dom'
import PageContainer from '../../shared/components/PageContainer'
import { useTranslate } from '../../shared/hooks/useTranslate'
import { useStaffEvents } from './StaffEventsPage.hooks'
import styles from './StaffEventsPage.module.css'

export default function StaffEventsPage() {
	const { content, language } = useTranslate()
	const copy = content.eventsDashboard
	const { events, loadEvents, state } = useStaffEvents()

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
				{state === 'loaded' && events.length > 0 && <section className={styles.list} aria-label={copy.staffTitle}>
					{events.map((event) => <Link className={styles.card} key={event.event_id} to={`/staff/events/${event.event_id}`}>
						<div>
							<h2>{event.event_name || copy.eventTitleDefault}</h2>
							{event.requested_date && <p>{copy.eventRequestedDate}: {event.requested_date}</p>}
							{event.expected_guest_count != null && <p>{copy.eventGuestCount}: {event.expected_guest_count}</p>}
							<p>{copy.eventApprovedAt}: {new Intl.DateTimeFormat(language, { dateStyle: 'long' }).format(new Date(event.approved_at))}</p>
						</div>
						<span className={styles.cardIndicator} aria-hidden="true">→</span>
					</Link>)}
				</section>}
			</PageContainer>
		</main>
	)
}
