// Pathing
// _______
// src/app/layouts/PublicLayout.tsx

import { NavLink, Outlet } from 'react-router-dom'
import PageContainer from '../../shared/components/PageContainer'
import { usePublicNavigation } from './PublicLayout.hooks'
import './PublicLayout.css'

const summerHouseLinks = [
	{ label: 'Oversigt over sommerhuse', href: 'https://www.engestofte.com/da/sommerhuse-oversigt' },
	{ label: 'Hospitalet', href: 'https://www.engestofte.com/da/sommerhuse-hospitalet' },
	{ label: 'Hushovmesterboligen', href: 'https://www.engestofte.com/da/sommerhuse-hushovmesterens-bolig' },
	{ label: 'Fiskerhuset', href: 'https://www.engestofte.com/da/sommerhuse-fiskerhuset' },
	{ label: 'Skovløberhuset', href: 'https://www.engestofte.com/da/sommerhuse-skovloeberhuset' },
	{ label: 'Grevindens Hus', href: 'https://www.engestofte.com/da/sommerhuse-grevindenshus' },
]

const navigation = [
	{ label: 'Bryllup', href: 'https://www.engestofte.com/da/bryllup' },
	{ label: 'Fest', href: 'https://www.engestofte.com/da/fest' },
	{ label: 'Konference', href: 'https://www.engestofte.com/da/konference' },
	{ label: 'Jagt', href: 'https://www.engestofte.com/da/jagt' },
	{ label: 'Julemarked', href: 'https://www.engestofte.com/da/julemarked' },
	{ label: 'Om Engestofte', href: 'https://www.engestofte.com/da/om-engestofte' },
]

export default function PublicLayout() {
	const { isMenuOpen, toggleMenu, closeMenu } = usePublicNavigation()

	return (
		<div className="site-shell">
			<header className="site-header">
				<PageContainer className="site-header__inner">
					<NavLink className="site-brand" to="/kontakt" aria-label="Engestofte Gods, forsiden">
						<img src="/images/shared/logo-white.png" alt="Engestofte Gods" />
					</NavLink>
					<div className={`site-menu${isMenuOpen ? ' is-open' : ''}`}>
						<button className="site-menu__toggle" type="button" aria-label={isMenuOpen ? 'Luk navigation' : 'Åbn navigation'} aria-expanded={isMenuOpen} onClick={toggleMenu}>
							<span aria-hidden="true" />
							<span aria-hidden="true" />
							<span aria-hidden="true" />
						</button>
						<div className="site-menu__content">
							<nav className="site-nav" aria-label="Hovednavigation">
								{navigation.map((item) => (
									<a className="site-nav__link" key={item.href} href={item.href} onClick={closeMenu}>{item.label}</a>
								))}
								<details className="site-nav__dropdown">
									<summary className="site-nav__link">Sommerhuse <span aria-hidden="true">⌄</span></summary>
									<div className="site-nav__submenu">
										{summerHouseLinks.map((item) => (
											<a key={item.href} href={item.href} onClick={closeMenu}>{item.label}</a>
										))}
									</div>
								</details>
							</nav>
							<div className="site-actions">
								<a className="site-action site-action--secondary" href="https://vaerkstedet-engestofte.dk/" target="_blank" rel="noreferrer" onClick={closeMenu}>Restaurant Værkstedet <span aria-hidden="true">↗</span></a>
								<NavLink className="site-action site-action--primary" to="/kontakt" onClick={closeMenu}>Kontakt</NavLink>
								<label className="language-picker">
									<span className="visually-hidden">Vælg sprog</span>
									<select aria-label="Vælg sprog" defaultValue="da" onChange={closeMenu}>
										<option value="da">DA</option>
										<option value="en">EN</option>
										<option value="de">DE</option>
									</select>
								</label>
							</div>
						</div>
					</div>
				</PageContainer>
			</header>
			<main className="site-main">
				<Outlet />
			</main>
			<footer className="site-footer">
				<PageContainer className="site-footer__inner">
					<span>Engestofte Gods <span aria-hidden="true">·</span> Søvej 10, 4930 Maribo</span>
					<nav className="site-footer__nav" aria-label="Forespørgselsportal">
						<NavLink to="/ai-flow">Start forespørgsel</NavLink>
						<NavLink to="/login">Log ind</NavLink>
						<NavLink to="/register">Opret konto</NavLink>
						<NavLink to="/dashboard/events/">Mine forespørgsler</NavLink>
					</nav>
				</PageContainer>
			</footer>
		</div>
	)
}
