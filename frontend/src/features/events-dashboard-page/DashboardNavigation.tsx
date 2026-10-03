// Pathing
// _______
// src/features/events-dashboard-page/DashboardNavigation.tsx

import { Link, NavLink } from 'react-router-dom'
import PageContainer from '../../shared/components/PageContainer'
import { useTranslate } from '../../shared/hooks/useTranslate'
import type { Language } from '../../shared/data/i18n/types'
import { useDashboardNavigation } from './DashboardNavigation.hooks'
import styles from './DashboardNavigation.module.css'

export default function DashboardNavigation() {
	const { content, language, setLanguage } = useTranslate()
	const copy = content.eventsDashboard
	const { logout, role } = useDashboardNavigation()
	const navigationLabel = role === 'CUSTOMER'
		? copy.navCustomerRequests
		: role === 'STAFF'
			? copy.navStaffEvents
			: role === 'OWNER'
				? copy.navOwnerRequests
				: null

	return (
		<header className={styles.header}>
			<PageContainer className={styles.inner}>
				<Link className={styles.brand} to="/dashboard/events/" aria-label={copy.brand}>
					<img src="/images/shared/logo-white.png" alt="" />
				</Link>
				<div className={styles.workspace}>
					{navigationLabel && <nav className={styles.navigation} aria-label={copy.title}>
						{role === 'CUSTOMER'
							? <NavLink className={({ isActive }) => isActive ? styles.activeLink : styles.link} to="/dashboard/events/">{navigationLabel}</NavLink>
							: <span className={styles.disabledLink} aria-disabled="true">{navigationLabel}</span>}
					</nav>}
					<div className={styles.actions}>
						<Link className={styles.action} to="/kontakt">{copy.toFront}</Link>
						{role ? <button className={styles.action} type="button" onClick={logout}>{copy.logout}</button> : <Link className={styles.action} to="/login">{content.auth.loginTitle}</Link>}
						<label className={styles.language}>
							<select aria-label={content.navigation.languageLabel} value={language} onChange={(event) => setLanguage(event.currentTarget.value as Language)}>
								<option value="da">DA</option>
								<option value="en">EN</option>
								<option value="de">DE</option>
							</select>
						</label>
					</div>
				</div>
			</PageContainer>
		</header>
	)
}
