// Pathing
// _______
// src/app/pages/PlaceholderPage.tsx

import type { FormEvent } from 'react'
import { Link } from 'react-router-dom'
import { Button, Card, EmptyState } from '../../shared/components/ui'
import InputText from '../../shared/components/input-text/InputText'
import PageContainer from '../../shared/components/PageContainer'
import Submit from '../../shared/components/submit/Submit'
import './PlaceholderPage.css'

type PlaceholderPageProps = {
	route: string
}

const contactPeople = [
	{
		name: 'Mette Egeskov',
		role: 'Adm. direktør, Engestofte',
		description: 'Kontakt Mette vedrørende overordnet drift og Madens Folkemøde',
		email: 'me@engestofte.dk',
		phone: '26 22 04 04',
		telephoneLink: 'tel:+4526220404',
		image: '/images/contact-page/woman-1.png',
	},
	{
		name: 'Lise Egeskov',
		role: 'Event Direktør',
		description: 'Kontakt Lise vedrørende jagter og julemarkeder',
		email: 'le@engestofte.dk',
		phone: '26 80 61 69',
		telephoneLink: 'tel:+4526806169',
		image: '/images/contact-page/woman-1.png',
	},
	{
		name: 'Johan Borup Jensen',
		role: 'Event Manager',
		description: 'Kontakt Johan vedrørende konferencer, fester og bryllupper',
		email: 'jj@engestofte.dk',
		phone: '31 37 54 59',
		telephoneLink: 'tel:+4531375459',
		image: '/images/contact-page/man-1.png',
	},
]

export default function PlaceholderPage({ route }: PlaceholderPageProps) {
	if (route === '/kontakt') {
		return (
			<PageContainer>
				<section className="contact-hero" aria-labelledby="contact-title">
					<div className="contact-hero__copy">
						<p className="eyebrow">En særlig dag begynder her</p>
						<h1 id="contact-title">Begivenheder på Engestofte Gods</h1>
						<p className="contact-hero__intro">
							Vi hjælper gerne med spørgsmål om arrangementer, ophold og oplevelser på Engestofte Gods.
						</p>
						<div className="contact-hero__actions">
							<Button as="link" to="/ai-flow" variant="primary">Begynd jeres forespørgsel</Button>
							<a className="ui-button ui-button--secondary" href="mailto:mail@engestofte.dk">Kontakt os</a>
						</div>
						<p className="contact-hero__note">Uforpligtende · tager cirka 5 minutter</p>
					</div>
					<div className="contact-hero__image-wrap">
						<img className="contact-hero__image" src="/images/contact-page/hero.png" alt="Engestofte Gods omgivet af grønne landskaber" />
						<div className="contact-hero__image-caption">Engestofte Gods <span>·</span> Lolland</div>
					</div>
				</section>
				<section className="contact-people" aria-labelledby="contact-people-title">
					<div className="contact-section-heading">
						<p className="eyebrow">Vi er her for at hjælpe</p>
						<h2 id="contact-people-title">Personlig rådgivning til dit event</h2>
					</div>
					<div className="contact-people__grid">
						{contactPeople.map((person) => (
							<article className="contact-person" key={person.email}>
								<div className="contact-person__portrait">
									<img src={person.image} alt={`Portræt af ${person.name}`} loading="lazy" decoding="async" />
								</div>
								<div className="contact-person__content">
									<h3>{person.name}</h3>
									<p className="contact-person__role">{person.role}</p>
									<p className="contact-person__description">{person.description}</p>
									<div className="contact-person__details">
										<a href={`mailto:${person.email}`}>{person.email}</a>
										<a href={person.telephoneLink}>T. {person.phone}</a>
									</div>
								</div>
							</article>
						))}
					</div>
				</section>
				<section className="contact-location" aria-labelledby="contact-location-title">
					<div className="contact-section-heading">
						<p className="eyebrow">Besøg os</p>
						<h2 id="contact-location-title">Find vej</h2>
						<p>Engestofte Gods, Søvej 10, 4930 Maribo</p>
					</div>
					<div className="contact-location__map">
						<iframe
							title="Kort over Engestofte Gods, Søvej 10, 4930 Maribo"
							src="https://maps.google.com/maps?q=Engestofte%20Gods%2C%20S%C3%B8vej%2010%2C%204930%20Maribo&output=embed"
							loading="lazy"
							referrerPolicy="no-referrer-when-downgrade"
							allowFullScreen
						/>
					</div>
					<a className="contact-location__directions" href="https://www.google.com/maps/search/?api=1&query=Engestofte%20Gods%2C%20S%C3%B8vej%2010%2C%204930%20Maribo" target="_blank" rel="noreferrer">
						Åbn rutevejledning <span aria-hidden="true">↗</span>
					</a>
				</section>
				<section className="contact-message" aria-labelledby="contact-message-title">
					<div className="contact-message__copy">
						<p className="eyebrow">Har du et spørgsmål?</p>
						<h2 id="contact-message-title">Send os en besked</h2>
						<p>Hvis du blot vil sende os en kort mail, kan du skrive direkte til Engestofte Gods.</p>
					</div>
					<form className="contact-message__form" onSubmit={submitContactMessage}>
						<InputText label="Navn" name="name" autoComplete="name" required />
						<InputText label="E-mail" name="email" type="email" autoComplete="email" required />
						<InputText label="Besked" name="message" multiline required />
						<Submit>Send besked</Submit>
					</form>
				</section>
			</PageContainer>
		)
	}

	const title = route === '404' ? 'Siden blev ikke fundet' : routeLabel(route)
	return (
		<PageContainer className="placeholder-page">
			<p className="eyebrow">Engestofte Gods · bryllupsforespørgsel</p>
			<h1>{title}</h1>
			<Card className="placeholder-card">
				<EmptyState
					title={route === '404' ? 'Vi kan ikke finde den adresse' : 'Denne del åbner snart'}
					description={route === '404' ? 'Gå tilbage til kontaktsiden og start herfra.' : `Her kommer ${title.toLocaleLowerCase('da')}. Du kan gå tilbage til kontaktsiden imens.`}
					action={<Button as="link" to="/kontakt" variant="secondary">Til kontaktsiden</Button>}
				/>
			</Card>
			{route !== '404' && <p className="route-caption">Rute: <code>{route}</code></p>}
			<Link className="visually-hidden" to="/kontakt">Engestofte Gods</Link>
		</PageContainer>
	)
}

function submitContactMessage(event: FormEvent<HTMLFormElement>) {
	event.preventDefault()
	const formData = new FormData(event.currentTarget)
	const name = String(formData.get('name') ?? '')
	const email = String(formData.get('email') ?? '')
	const message = String(formData.get('message') ?? '')
	const subject = encodeURIComponent('Besked fra kontaktformularen på Engestofte Gods')
	const body = encodeURIComponent(`Navn: ${name}\nE-mail: ${email}\n\n${message}`)
	window.location.assign(`mailto:mail@engestofte.dk?subject=${subject}&body=${body}`)
}

function routeLabel(route: string) {
	const labels: Record<string, string> = {
		'/ai-flow': 'Jeres bryllup, jeres ønsker',
		'/login': 'Log ind',
		'/register': 'Opret konto',
		'/dashboard/events/': 'Mine forespørgsler',
		'/dashboard/events/:id': 'Forespørgselsdetaljer',
	}
	return labels[route] ?? 'Engestofte Gods'
}
