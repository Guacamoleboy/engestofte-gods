// Pathing
// _______
// src/features/guest-invitation-page/GuestInvitationPage.tsx

import { useTranslate } from '../../shared/hooks/useTranslate'
import { useGuestInvitation } from './GuestInvitationPage.hooks'
import styles from './GuestInvitationPage.module.css'

const invitationThemes = {
	WEDDING: styles.weddingTheme,
}

export default function GuestInvitationPage() {
	const { content, language } = useTranslate()
	const copy = content.guestInvitation
	const { invitation, loadInvitation, state } = useGuestInvitation()

	if (state !== 'loaded' || !invitation) {
		return <main className={styles.page}>
			<section className={styles.state} role={state === 'loading' ? 'status' : 'alert'}>
				<p>{state === 'loading' ? copy.loading : copy.unavailable}</p>
				{state === 'unavailable' && <button type="button" onClick={() => void loadInvitation()}>{copy.retry}</button>}
			</section>
		</main>
	}

	const theme = invitationThemes[invitation.category] ?? styles.defaultTheme
	const primaryContactName = invitation.primary_contact_name || copy.contactFallback
	const programItems = copy.programItems

	return <main className={`${styles.page} ${theme}`}>
		<article className={styles.flyer}>
			<header className={styles.hero}>
				<div className={styles.heroImageFrame}>
					<img className={styles.heroImage} src="/images/invitations/hero-1.jpg" alt={copy.heroAlt} />
				</div>
				<div className={styles.heroContent}>
					<p className={styles.brand}>{copy.brand}</p>
					<p className={styles.eyebrow}>{copy.invitationEyebrow}</p>
					<span className={styles.ornament} aria-hidden="true">✳</span>
					<h1 id="invitation-title">{invitation.event_name || copy.fallbackTitle}</h1>
					<p className={styles.welcome}>{copy.welcome}</p>
				</div>
			</header>

			<section className={styles.invitationDetails} aria-labelledby="invitation-details-title">
				<p className={styles.sectionEyebrow}>{copy.categoryFallback}</p>
				<h2 id="invitation-details-title">{copy.detailsTitle}</h2>
				<div className={styles.rule} aria-hidden="true"><span>✦</span></div>
				<div className={styles.detailGrid}>
					<article className={styles.detailCard}>
						<span className={styles.detailIcon} aria-hidden="true">◷</span>
						<h3>{copy.dateLabel}</h3>
						<p>{invitation.requested_date || copy.datePending}</p>
					</article>
					<article className={styles.detailCard}>
						<span className={styles.detailIcon} aria-hidden="true">⌖</span>
						<h3>{copy.venueLabel}</h3>
						<p>{copy.brand}</p>
					</article>
					<article className={styles.detailCard}>
						<span className={styles.detailIcon} aria-hidden="true">♧</span>
						<h3>{copy.guestCountLabel}</h3>
						<p>{invitation.expected_guest_count == null ? copy.guestCountPending : new Intl.NumberFormat(language).format(invitation.expected_guest_count)}</p>
					</article>
				</div>
			</section>

			<section className={styles.programSection} aria-labelledby="program-title">
				<p className={styles.sectionEyebrow}>{copy.programEyebrow}</p>
				<h2 id="program-title">{copy.programTitle}</h2>
				<div className={styles.rule} aria-hidden="true"><span>✦</span></div>
				<ol className={styles.timeline}>
					{programItems.map((item, index) => <li key={item.title}>
						<span className={styles.timelineMarker} aria-hidden="true">{String(index + 1).padStart(2, '0')}</span>
						<div><h3>{item.title}</h3><p>{item.description}</p></div>
					</li>)}
				</ol>
			</section>

			<section className={styles.practicalSection} aria-labelledby="practical-title">
				<div className={styles.practicalCard}>
					<p className={styles.sectionEyebrow}>{copy.practicalEyebrow}</p>
					<h2 id="practical-title">{copy.practicalTitle}</h2>
					<p>{copy.practicalDescription}</p>
					<div className={styles.contactCard}>
						<h3>{copy.contactTitle}</h3>
						{invitation.primary_contact_name && <p>{invitation.primary_contact_name}</p>}
						{invitation.primary_contact_email && <a href={`mailto:${invitation.primary_contact_email}`}>{invitation.primary_contact_email}</a>}
						<p>{copy.additionalInformation.replace('{name}', primaryContactName)} <a href="mailto:mail@engestofte.dk">mail@engestofte.dk</a>.</p>
					</div>
				</div>
			</section>

			<footer className={styles.closing}>{copy.footer}<span aria-hidden="true">✦</span></footer>
		</article>
	</main>
}
