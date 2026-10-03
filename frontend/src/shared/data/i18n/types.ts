// Pathing
// _______
// src/shared/data/i18n/types.ts

export type Language = 'da' | 'en' | 'de'

export type TranslationContent = {
	aiFlow: {
		introMessage: string
		flowProgress: string
		flowComplete: string
		summaryTitle: string
		summaryQuestion: string
		summaryAnswer: string
		cancelRequest: string
		goBack: string
		stepQuestions: string[]
		answerLabel: string
		answerPlaceholder: string
		assistantName: string
		customerName: string
		statusSending: string
		statusSent: string
		statusReceived: string
		statusNew: string
		statusFailed: string
		aiUnavailable: string
		error: string
		pending: string
		submit: string
		outOfScopeContact: string
		finished: string
		contactViaWebsite: string
		emailUs: string
	}
	navigation: {
		label: string
		openMenu: string
		closeMenu: string
		languageLabel: string
		contact: string
		restaurant: string
		wedding: string
		party: string
		conference: string
		hunting: string
		christmasMarket: string
		about: string
		summerHouses: string
		summerHouseLinks: string[]
	}
	contact: {
		heroEyebrow: string
		heroTitle: string
		heroIntro: string
		startEnquiry: string
		contactUs: string
		heroNote: string
		heroImageAlt: string
		heroCaption: string
		peopleEyebrow: string
		peopleTitle: string
		peopleImageAlt: string
		people: { role: string; description: string }[]
		phonePrefix: string
		locationEyebrow: string
		findWay: string
		address: string
		mapTitle: string
		directions: string
		messageEyebrow: string
		messageTitle: string
		messageIntro: string
		nameLabel: string
		emailLabel: string
		messageLabel: string
		sendMessage: string
		mailSubject: string
	}
	footer: {
		quickLinks: string
		contact: string
		email: string
		phone: string
		socialLinks: string
		facebook: string
		instagram: string
		newsletterTitle: string
		newsletterDescription: string
		newsletterLabel: string
		newsletterSubmit: string
		newsletterSubject: string
		newsletterBody: string
		legal: string
		address: string
		personalDataPolicy: string
		smileyReport: string
	}
	placeholder: {
		eyebrow: string
		notFound: string
		notFoundDescription: string
		comingSoon: string
		comingSoonDescription: string
		backToContact: string
		routeLabel: string
		routes: Record<string, string>
	}
}
