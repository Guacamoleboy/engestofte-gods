// Pathing
// _______
// src/features/public-navigation/usePublicNavigation.ts

import { useState, type FormEvent } from 'react'
import { useTranslate } from '../../shared/hooks/useTranslate'

export function usePublicNavigation() {
	const [isMenuOpen, setIsMenuOpen] = useState(false)
	const translation = useTranslate()
	function localizeHref(href: string) {
		return href.replace('/da/', `/${translation.language}/`)
	}
	function submitNewsletter(event: FormEvent<HTMLFormElement>, subjectText: string, bodyText: string) {
		event.preventDefault()
		const formData = new FormData(event.currentTarget)
		const email = String(formData.get('newsletterEmail') ?? '')
		const subject = encodeURIComponent(subjectText)
		const body = encodeURIComponent(`${bodyText} ${email}`)
		window.location.assign(`mailto:mail@engestofte.dk?subject=${subject}&body=${body}`)
	}
	return {
		...translation,
		isMenuOpen,
		toggleMenu: () => setIsMenuOpen((isOpen) => !isOpen),
		closeMenu: () => setIsMenuOpen(false),
		localizeHref,
		submitNewsletter,
	}
}
