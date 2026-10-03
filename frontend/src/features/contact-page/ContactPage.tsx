// Pathing
// _______
// src/features/contact-page/ContactPage.tsx

import { Button } from '../../shared/components/ui'
import InputText from '../../shared/components/input-text/InputText'
import PageContainer from '../../shared/components/PageContainer'
import Submit from '../../shared/components/submit/Submit'
import { getAiFlowDraftStatus } from '../../shared/data/aiFlowDraft'
import { useTranslate } from '../../shared/hooks/useTranslate'
import { useAiFlowTransition } from '../ai-flow-transition/AiFlowTransitionContext'
import { useContactPage } from './ContactPage.hooks'
import styles from './ContactPage.module.css'

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
		image: '/images/contact-page/woman-2.png',
	},
	{
		name: 'Johan Borup Jensen',
		email: 'jj@engestofte.dk',
		phone: '31 37 54 59',
		telephoneLink: 'tel:+4531375459',
		image: '/images/contact-page/man-1.png',
	},
]

export default function ContactPage() {
	const { content: copy } = useTranslate()
	const { submitContactMessage } = useContactPage()
	const { startEntryTransition, startFinalTransition } = useAiFlowTransition()

	function startEnquiry() {
		const draftStatus = getAiFlowDraftStatus()
		if (draftStatus !== 'none') {
			startFinalTransition('', draftStatus)
			return
		}
		startEntryTransition()
	}

	return (
		<PageContainer>
			<section className={styles['contact-hero']} aria-labelledby="contact-title">
				<div className={styles['contact-hero__copy']}>
					<p className={styles['eyebrow']}>{copy.contact.heroEyebrow}</p>
					<h1 id="contact-title">{copy.contact.heroTitle}</h1>
					<p className={styles['contact-hero__intro']}>{copy.contact.heroIntro}</p>
					<div className={styles['contact-hero__actions']}>
						<Button variant="primary" onClick={startEnquiry}>{copy.contact.startEnquiry}</Button>
						<a className="ui-button ui-button--secondary" href="mailto:mail@engestofte.dk">{copy.contact.contactUs}</a>
					</div>
					<p className={styles['contact-hero__note']}>{copy.contact.heroNote}</p>
				</div>
				<div className={styles['contact-hero__image-wrap']}>
					<img className={styles['contact-hero__image']} src="/images/contact-page/hero.png" alt={copy.contact.heroImageAlt} />
					<div className={styles['contact-hero__image-caption']}>{copy.contact.heroCaption}</div>
				</div>
			</section>
			<section className={styles['contact-people']} aria-labelledby="contact-people-title">
				<div className={styles['contact-section-heading']}>
					<p className={styles['eyebrow']}>{copy.contact.peopleEyebrow}</p>
					<h2 id="contact-people-title">{copy.contact.peopleTitle}</h2>
				</div>
				<div className={styles['contact-people__grid']}>
					{contactPeople.map((person, index) => (
						<article className={styles['contact-person']} key={person.email}>
							<div className={styles['contact-person__portrait']}>
								<img src={person.image} alt={copy.contact.peopleImageAlt.replace('{name}', person.name)} loading="lazy" decoding="async" />
							</div>
							<div className={styles['contact-person__content']}>
								<h3>{person.name}</h3>
								<p className={styles['contact-person__role']}>{copy.contact.people[index].role}</p>
								<p className={styles['contact-person__description']}>{copy.contact.people[index].description}</p>
								<div className={styles['contact-person__details']}>
									<a href={`mailto:${person.email}`}>{person.email}</a>
									<a href={person.telephoneLink}>{copy.contact.phonePrefix} {person.phone}</a>
								</div>
							</div>
						</article>
					))}
				</div>
			</section>
			<section className={styles['contact-location']} aria-labelledby="contact-location-title">
				<div className={styles['contact-section-heading']}>
					<p className={styles['eyebrow']}>{copy.contact.locationEyebrow}</p>
					<h2 id="contact-location-title">{copy.contact.findWay}</h2>
					<p>{copy.contact.address}</p>
				</div>
				<div className={styles['contact-location__map']}>
					<iframe
						title={copy.contact.mapTitle}
						src="https://maps.google.com/maps?q=Engestofte%20Gods%2C%20S%C3%B8vej%2010%2C%204930%20Maribo&output=embed"
						loading="lazy"
						referrerPolicy="no-referrer-when-downgrade"
						allowFullScreen
					/>
				</div>
				<a className={styles['contact-location__directions']} href="https://www.google.com/maps/search/?api=1&query=Engestofte%20Gods%2C%20S%C3%B8vej%2010%2C%204930%20Maribo" target="_blank" rel="noreferrer">
					{copy.contact.directions} <span aria-hidden="true">↗</span>
				</a>
			</section>
			<section className={styles['contact-message']} aria-labelledby="contact-message-title">
				<div className={styles['contact-message__copy']}>
					<p className={styles['eyebrow']}>{copy.contact.messageEyebrow}</p>
					<h2 id="contact-message-title">{copy.contact.messageTitle}</h2>
					<p>{copy.contact.messageIntro}</p>
				</div>
				<form className={styles['contact-message__form']} onSubmit={(event) => submitContactMessage(event, copy.contact)}>
					<InputText label={copy.contact.nameLabel} name="name" autoComplete="name" required />
					<InputText label={copy.contact.emailLabel} name="email" type="email" autoComplete="email" required />
					<InputText label={copy.contact.messageLabel} name="message" multiline required />
					<Submit>{copy.contact.sendMessage}</Submit>
				</form>
			</section>
		</PageContainer>
	)
}
