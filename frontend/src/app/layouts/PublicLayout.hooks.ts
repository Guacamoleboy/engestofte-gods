// Pathing
// _______
// src/app/layouts/PublicLayout.hooks.ts

import { useEffect, useState } from 'react'
import daTranslation from '../../shared/data/i18n/da.json'
import { loadTranslations } from '../../shared/data/i18n/translations'
import type { Language, TranslationContent } from '../../shared/data/i18n/types'

export function usePublicNavigation() {
	const [isMenuOpen, setIsMenuOpen] = useState(false)
	const [language, setLanguage] = useState<Language>('da')
	const [content, setContent] = useState<TranslationContent>(daTranslation)

	useEffect(() => {
		document.documentElement.lang = language
		if (language === 'da') {
			setContent(daTranslation)
			return
		}

		let isCurrentLanguage = true

		async function updateContent() {
			const translation = await loadTranslations(language)
			if (isCurrentLanguage) setContent(translation)
		}

		void updateContent()
		return () => { isCurrentLanguage = false }
	}, [language])

	function toggleMenu() {
		setIsMenuOpen((isOpen) => !isOpen)
	}

	function closeMenu() {
		setIsMenuOpen(false)
	}

	return { isMenuOpen, toggleMenu, closeMenu, language, setLanguage, content }
}
