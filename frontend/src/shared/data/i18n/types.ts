// Pathing
// _______
// src/shared/data/i18n/types.ts

export type Language = 'da' | 'en' | 'de'

export type TranslationContent = {
	auth: {
		eyebrow: string
		loginTitle: string
		loginDescription: string
		registerTitle: string
		registerDescription: string
		forgotTitle: string
		forgotDescription: string
		name: string
		email: string
		password: string
		loginSubmit: string
		registerSubmit: string
		submitting: string
		forgotLink: string
		backToLogin: string
		haveAccount: string
		needAccount: string
	}
	aiFlow: {
		introMessage: string
		flowProgress: string
		flowComplete: string
		draftResumeNotice: string
		draftUnreadableNotice: string
		draftCompleteNotice: string
		startNewDraft: string
		confirmNewDraft: string
		continueDraft: string
		draftStorageNotice: string
		cancelRequest: string
		goBack: string
		stepQuestions: string[]
		weddingDirectionOptions: { title: string; description: string; choice: string }[]
		weddingDirectionOptionsLabel: string
		intimateRecommendation: string
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
		finalMessage: string
		contactViaWebsite: string
		redirect: {
			title: string
			description: string
			contactExplanation: string
			createAccount: string
			login: string
			sendEmail: string
			cancelEnquiry: string
		}
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
