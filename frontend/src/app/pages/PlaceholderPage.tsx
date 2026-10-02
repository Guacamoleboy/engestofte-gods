// Pathing
// _______
// src/app/pages/PlaceholderPage.tsx

import type { FormEvent } from 'react'
import { Link } from 'react-router-dom'
import { Button, Card, EmptyState } from '../../shared/components/ui'
import InputText from '../../shared/components/input-text/InputText'
import PageContainer from '../../shared/components/PageContainer'
import Submit from '../../shared/components/submit/Submit'
import type { TranslationContent } from '../../shared/data/i18n/types'
import { usePageContent } from './PlaceholderPage.hooks'
import './PlaceholderPage.css'

type PlaceholderPageProps = {
	route: string
}

const contactPeople = [
	{
		name: 'Mette Egeskov',
		email: 'me@engestofte.dk',
		phone: '26 22 04 04',
		telephoneLink: 'tel:+4526220404',
		image: '/images/contact-page/woman-1.png',
	},
	{
		name: 'Lise Egeskov',
		email: 'le@engestofte.dk',
		phone: '26 80 61 69',
		telephoneLink: 'tel:+4526806169',
		image: '/images/contact-page/woman-1.png',
	},
	{
		name: 'Johan Borup Jensen',
		email: 'jj@engestofte.dk',
		phone: '31 37 54 59',
		telephoneLink: 'tel:+4531375459',
		image: '/images/contact-page/man-1.png',
	},
]

export default function PlaceholderPage({ route }: PlaceholderPageProps) {
	const { content: copy } = usePageContent()

	if (route === '/kontakt') {
		return (
			<PageContainer>
				<section className="contact-hero" aria-labelledby="contact-title">
					<div className="contact-hero__copy">
						<p className="eyebrow">{copy.contact.heroEyebrow}</p>
						<h1 id="contact-title">{copy.contact.heroTitle}</h1>
						<p className="contact-hero__intro">{copy.contact.heroIntro}</p>
						<div className="contact-hero__actions">
							<Button as="link" to="/ai-flow" variant="primary">{copy.contact.startEnquiry}</Button>
							<a className="ui-button ui-button--secondary" href="mailto:mail@engestofte.dk">{copy.contact.contactUs}</a>
						</div>
						<p className="contact-hero__note">{copy.contact.heroNote}</p>
					</div>
					<div className="contact-hero__image-wrap">
						<img className="contact-hero__image" src="/images/contact-page/hero.png" alt={copy.contact.heroImageAlt} />
						<div className="contact-hero__image-caption">{copy.contact.heroCaption}</div>
					</div>
				</section>
				<section className="contact-people" aria-labelledby="contact-people-title">
					<div className="contact-section-heading">
						<p className="eyebrow">{copy.contact.peopleEyebrow}</p>
						<h2 id="contact-people-title">{copy.contact.peopleTitle}</h2>
					</div>
					<div className="contact-people__grid">
						{contactPeople.map((person, index) => (
							<article className="contact-person" key={person.email}>
								<div className="contact-person__portrait">
									<img src={person.image} alt={copy.contact.peopleImageAlt.replace('{name}', person.name)} loading="lazy" decoding="async" />
								</div>
								<div className="contact-person__content">
									<h3>{person.name}</h3>
									<p className="contact-person__role">{copy.contact.people[index].role}</p>
									<p className="contact-person__description">{copy.contact.people[index].description}</p>
									<div className="contact-person__details">
										<a href={`mailto:${person.email}`}>{person.email}</a>
										<a href={person.telephoneLink}>{copy.contact.phonePrefix} {person.phone}</a>
									</div>
								</div>
							</article>
						))}
					</div>
				</section>
				<section className="contact-location" aria-labelledby="contact-location-title">
					<div className="contact-section-heading">
						<p className="eyebrow">{copy.contact.locationEyebrow}</p>
						<h2 id="contact-location-title">{copy.contact.findWay}</h2>
						<p>{copy.contact.address}</p>
					</div>
					<div className="contact-location__map">
						<iframe
							title={copy.contact.mapTitle}
							src="https://maps.google.com/maps?q=Engestofte%20Gods%2C%20S%C3%B8vej%2010%2C%204930%20Maribo&output=embed"
							loading="lazy"
							referrerPolicy="no-referrer-when-downgrade"
							allowFullScreen
						/>
					</div>
					<a className="contact-location__directions" href="https://www.google.com/maps/search/?api=1&query=Engestofte%20Gods%2C%20S%C3%B8vej%2010%2C%204930%20Maribo" target="_blank" rel="noreferrer">
						{copy.contact.directions} <span aria-hidden="true">↗</span>
					</a>
				</section>
				<section className="contact-message" aria-labelledby="contact-message-title">
					<div className="contact-message__copy">
						<p className="eyebrow">{copy.contact.messageEyebrow}</p>
						<h2 id="contact-message-title">{copy.contact.messageTitle}</h2>
						<p>{copy.contact.messageIntro}</p>
					</div>
					<form className="contact-message__form" onSubmit={(event) => submitContactMessage(event, copy.contact)}>
						<InputText label={copy.contact.nameLabel} name="name" autoComplete="name" required />
						<InputText label={copy.contact.emailLabel} name="email" type="email" autoComplete="email" required />
						<InputText label={copy.contact.messageLabel} name="message" multiline required />
						<Submit>{copy.contact.sendMessage}</Submit>
					</form>
				</section>
			</PageContainer>
		)
	}

	const title = route === '404' ? copy.placeholder.notFound : copy.placeholder.routes[route] ?? 'Engestofte Gods'
	return (
		<PageContainer className="placeholder-page">
			<p className="eyebrow">{copy.placeholder.eyebrow}</p>
			<h1>{title}</h1>
			<Card className="placeholder-card">
				<EmptyState
					title={route === '404' ? copy.placeholder.notFound : copy.placeholder.comingSoon}
					description={route === '404' ? copy.placeholder.notFoundDescription : copy.placeholder.comingSoonDescription.replace('{title}', title)}
					action={<Button as="link" to="/kontakt" variant="secondary">{copy.placeholder.backToContact}</Button>}
				/>
			</Card>
			{route !== '404' && <p className="route-caption">{copy.placeholder.routeLabel}: <code>{route}</code></p>}
			<Link className="visually-hidden" to="/kontakt">Engestofte Gods</Link>
		</PageContainer>
	)
}

function submitContactMessage(event: FormEvent<HTMLFormElement>, copy: TranslationContent['contact']) {
	event.preventDefault()
	const formData = new FormData(event.currentTarget)
	const name = String(formData.get('name') ?? '')
	const email = String(formData.get('email') ?? '')
	const message = String(formData.get('message') ?? '')
	const subject = encodeURIComponent(copy.mailSubject)
	const body = encodeURIComponent(`${copy.nameLabel}: ${name}\n${copy.emailLabel}: ${email}\n\n${message}`)
	window.location.assign(`mailto:mail@engestofte.dk?subject=${subject}&body=${body}`)
}
