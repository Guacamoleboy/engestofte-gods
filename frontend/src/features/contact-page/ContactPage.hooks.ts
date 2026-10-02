// Pathing
// _______
// src/features/contact-page/ContactPage.hooks.ts

import type { FormEvent } from 'react'
import type { TranslationContent } from '../../shared/data/i18n/types'

export function useContactPage() {
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

	return { submitContactMessage }
}
