// Pathing
// _______
// src/app/layouts/PublicLayout.tsx

import type { ChangeEvent, FormEvent } from 'react'
import { NavLink, Outlet } from 'react-router-dom'
import InputText from '../../shared/components/input-text/InputText'
import PageContainer from '../../shared/components/PageContainer'
import Submit from '../../shared/components/submit/Submit'
import type { Language } from '../../shared/data/i18n/types'
import { usePublicNavigation } from './PublicLayout.hooks'
import './PublicLayout.css'

const navigationItems = [
	{ key: 'wedding', href: 'https://www.engestofte.com/da/bryllup' },
	{ key: 'party', href: 'https://www.engestofte.com/da/fest' },
	{ key: 'conference', href: 'https://www.engestofte.com/da/konference' },
	{ key: 'hunting', href: 'https://www.engestofte.com/da/jagt' },
	{ key: 'christmasMarket', href: 'https://www.engestofte.com/da/julemarked' },
	{ key: 'about', href: 'https://www.engestofte.com/da/om-engestofte' },
] as const

const summerHouseLinks = [
	'https://www.engestofte.com/da/sommerhuse-oversigt',
	'https://www.engestofte.com/da/sommerhuse-hospitalet',
	'https://www.engestofte.com/da/sommerhuse-hushovmesterens-bolig',
	'https://www.engestofte.com/da/sommerhuse-fiskerhuset',
	'https://www.engestofte.com/da/sommerhuse-skovloeberhuset',
	'https://www.engestofte.com/da/sommerhuse-grevindenshus',
]

const smileyReportUrl = 'https://www.findsmiley.dk/Sider/KontrolRapport.aspx?Virk6887688='

export default function PublicLayout() {
	const { isMenuOpen, toggleMenu, closeMenu, language, setLanguage, content: copy } = usePublicNavigation()

	function handleLanguageChange(event: ChangeEvent<HTMLSelectElement>) {
		setLanguage(event.currentTarget.value as Language)
		closeMenu()
	}

	return (
		<div className="site-shell">
			<header className="site-header">
				<PageContainer className="site-header__inner">
					<NavLink className="site-brand" to="/kontakt" aria-label="Engestofte Gods">
						<img src="/images/shared/logo-white.png" alt="Engestofte Gods" />
					</NavLink>
					<div className={`site-menu${isMenuOpen ? ' is-open' : ''}`}>
						<button className="site-menu__toggle" type="button" aria-label={isMenuOpen ? copy.navigation.closeMenu : copy.navigation.openMenu} aria-expanded={isMenuOpen} onClick={toggleMenu}>
							<span aria-hidden="true" />
							<span aria-hidden="true" />
							<span aria-hidden="true" />
						</button>
						<div className="site-menu__content">
							<nav className="site-nav" aria-label={copy.navigation.label}>
								{navigationItems.map((item) => (
									<a className="site-nav__link" key={item.key} href={localizeHref(item.href, language)} onClick={closeMenu}>{copy.navigation[item.key]}</a>
								))}
								<details className="site-nav__dropdown">
									<summary className="site-nav__link">{copy.navigation.summerHouses} <span className="site-nav__chevron" aria-hidden="true" /></summary>
									<div className="site-nav__submenu">
										{summerHouseLinks.map((href, index) => (
											<a key={href} href={localizeHref(href, language)} onClick={closeMenu}>{copy.navigation.summerHouseLinks[index]}</a>
										))}
									</div>
								</details>
							</nav>
							<div className="site-actions">
								<a className="site-action site-action--secondary" href="https://vaerkstedet-engestofte.dk/" target="_blank" rel="noreferrer" onClick={closeMenu}>{copy.navigation.restaurant} <span aria-hidden="true">↗</span></a>
								<NavLink className="site-action site-action--primary" to="/kontakt" onClick={closeMenu}>{copy.navigation.contact}</NavLink>
								<label className="language-picker">
									<span className="visually-hidden">{copy.navigation.languageLabel}</span>
									<select aria-label={copy.navigation.languageLabel} value={language} onChange={handleLanguageChange}>
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
				<Outlet context={{ language, content: copy }} />
			</main>
			<footer className="site-footer">
				<PageContainer className="site-footer__inner">
					<section className="site-footer__column site-footer__column--quick-links" aria-labelledby="footer-quick-links-title">
						<h2 id="footer-quick-links-title">{copy.footer.quickLinks}</h2>
						<nav className="site-footer__nav" aria-label={copy.footer.quickLinks}>
							<NavLink to="/kontakt">{copy.footer.contact}</NavLink>
							<NavLink to="/ai-flow">{copy.contact.startEnquiry}</NavLink>
							<NavLink to="/login">{copy.placeholder.routes['/login']}</NavLink>
							<NavLink to="/register">{copy.placeholder.routes['/register']}</NavLink>
							<NavLink to="/dashboard/events/">{copy.placeholder.routes['/dashboard/events/']}</NavLink>
						</nav>
						<div className="site-footer__contact-links">
							<a href="mailto:mail@engestofte.dk">mail@engestofte.dk</a>
							<a href="tel:+4554490073">+45 54 49 00 73</a>
						</div>
					</section>
					<section className="site-footer__column site-footer__column--newsletter" aria-labelledby="footer-newsletter-title">
						<div className="site-footer__social" aria-label={copy.footer.socialLinks}>
							<a href="https://www.facebook.com/Engestofte" target="_blank" rel="noreferrer" aria-label={copy.footer.facebook}>
								<svg viewBox="0 0 24 24" aria-hidden="true"><path d="M13.5 21v-8.2h2.8l.4-3.2h-3.2V7.5c0-.9.3-1.5 1.6-1.5h1.7V3.1c-.3 0-1.3-.1-2.5-.1-2.5 0-4.2 1.5-4.2 4.3v2.3H7.3v3.2h2.8V21h3.4Z" /></svg>
							</a>
							<a href="https://www.instagram.com/engestoftegods/" target="_blank" rel="noreferrer" aria-label={copy.footer.instagram}>
								<svg viewBox="0 0 24 24" aria-hidden="true"><rect x="3.2" y="3.2" width="17.6" height="17.6" rx="5" fill="none" stroke="currentColor" strokeWidth="1.8" /><circle cx="12" cy="12" r="4.1" fill="none" stroke="currentColor" strokeWidth="1.8" /><circle cx="17.7" cy="6.5" r="1.1" /></svg>
							</a>
						</div>
						<h2 id="footer-newsletter-title">{copy.footer.newsletterTitle}</h2>
						<p>{copy.footer.newsletterDescription}</p>
						<form className="site-footer__newsletter-form" onSubmit={(event) => submitNewsletter(event, copy.footer.newsletterSubject, copy.footer.newsletterBody)}>
							<InputText label={copy.footer.newsletterLabel} name="newsletterEmail" type="email" autoComplete="email" required />
							<Submit className="site-footer__newsletter-submit">{copy.footer.newsletterSubmit}</Submit>
						</form>
					</section>
					<section className="site-footer__column site-footer__column--legal" aria-labelledby="footer-legal-title">
						<h2 id="footer-legal-title">{copy.footer.legal}</h2>
						<address>{copy.footer.address}</address>
						<nav className="site-footer__legal-links" aria-label={copy.footer.legal}>
							<a href={`https://www.engestofte.com/${language}/persondatapolitik`} target="_blank" rel="noreferrer">{copy.footer.personalDataPolicy}</a>
							<a href={smileyReportUrl} target="_blank" rel="noreferrer">{copy.footer.smileyReport}</a>
						</nav>
					</section>
				</PageContainer>
			</footer>
		</div>
	)
}

function localizeHref(href: string, language: Language) {
	return href.replace('/da/', `/${language}/`)
}

function submitNewsletter(event: FormEvent<HTMLFormElement>, subjectText: string, bodyText: string) {
	event.preventDefault()
	const formData = new FormData(event.currentTarget)
	const email = String(formData.get('newsletterEmail') ?? '')
	const subject = encodeURIComponent(subjectText)
	const body = encodeURIComponent(`${bodyText} ${email}`)
	window.location.assign(`mailto:mail@engestofte.dk?subject=${subject}&body=${body}`)
}
