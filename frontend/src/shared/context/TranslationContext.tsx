// Pathing
// _______
// src/shared/context/TranslationContext.tsx

import { createContext, useEffect, useState, type PropsWithChildren } from 'react'
import daTranslation from '../data/i18n/da.json'
import { AI_FLOW_DRAFT } from '../data/aiFlowDraft'
import { loadTranslations } from '../data/i18n/translations'
import type { Language, TranslationContent } from '../data/i18n/types'

function getInitialLanguage(): Language {
	try {
		const draft: unknown = JSON.parse(window.localStorage.getItem(AI_FLOW_DRAFT.key) ?? 'null')
		if (draft && typeof draft === 'object' && 'language' in draft && ['da', 'en', 'de'].includes(String(draft.language))) {
			return draft.language as Language
		}
	} catch {
		return 'da'
	}
	return 'da'
}

export const TranslationContext = createContext<{
	language: Language
	setLanguage: (language: Language) => void
	content: TranslationContent
} | null>(null)

export function TranslationProvider({ children }: PropsWithChildren) {
	const [language, setLanguage] = useState<Language>(getInitialLanguage)
	const [content, setContent] = useState<TranslationContent>(daTranslation)

	useEffect(() => {
		document.documentElement.lang = language
		if (language === 'da') {
			setContent(daTranslation)
			return
		}
		let active = true
		async function load() {
			const translation = await loadTranslations(language)
			if (active) setContent(translation)
		}
		void load()
		return () => { active = false }
	}, [language])

	return <TranslationContext.Provider value={{ language, setLanguage, content }}>{children}</TranslationContext.Provider>
}
