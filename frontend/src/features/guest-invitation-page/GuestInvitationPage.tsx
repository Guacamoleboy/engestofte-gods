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
	const { content } = useTranslate()
	const copy = content.guestInvitation
	const { invitation, loadInvitation, state } = useGuestInvitation()

	if (state !== 'loaded' || !invitation) {
		return <main className={styles.page}>
			<div className={styles.brand}>{copy.brand}</div>
			<section className={styles.state} role={state === 'loading' ? 'status' : 'alert'}>
				<p>{state === 'loading' ? copy.loading : copy.unavailable}</p>
				{state === 'unavailable' && <button type="button" onClick={() => void loadInvitation()}>{copy.retry}</button>}
			</section>
		</main>
	}

	const theme = invitationThemes[invitation.category] ?? styles.defaultTheme

	return <main className={`${styles.page} ${theme}`}>
		<section className={styles.hero} aria-labelledby="invitation-title">
			<img className={styles.heroImage} src="/images/invitations/hero-1.jpg" alt={copy.heroAlt} />
			<div className={styles.heroShade} />
			<div className={styles.brand}>{copy.brand}</div>
			<div className={styles.heroContent}>
				<p className={styles.eyebrow}>{copy.invitationEyebrow}</p>
				<span className={styles.ornament} aria-hidden="true">✳</span>
				<h1 id="invitation-title">{invitation.event_name || copy.fallbackTitle}</h1>
				<p className={styles.welcome}>{copy.welcome}</p>
				<a className={styles.scrollCue} href="#invitation-details">{copy.scrollCue}<span aria-hidden="true">↓</span></a>
			</div>
		</section>

		<section className={styles.invitationDetails} id="invitation-details">
			<p className={styles.sectionEyebrow}>{copy.categoryFallback}</p>
			<h2>{invitation.event_name || copy.fallbackTitle}</h2>
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
			</div>
		</section>

		<section className={styles.programSection}>
			<p className={styles.sectionEyebrow}>{copy.programTitle}</p>
			<div className={styles.rule} aria-hidden="true"><span>✦</span></div>
			<p>{copy.programDescription}</p>
		</section>

		<section className={styles.practicalSection}>
			<div className={styles.practicalCard}>
				<p className={styles.sectionEyebrow}>{copy.practicalTitle}</p>
				<p>{copy.practicalDescription}</p>
			</div>
		</section>

		<div className={styles.closing}>{copy.footer}<span aria-hidden="true">✦</span></div>
	</main>
}
