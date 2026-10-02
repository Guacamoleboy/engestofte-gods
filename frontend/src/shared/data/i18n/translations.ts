// Pathing
// _______
// src/shared/data/i18n/translations.ts

import type { Language, TranslationContent } from './types'

const translationLoaders: Record<Language, () => Promise<TranslationContent>> = {
	da: async () => (await import('./da.json')).default,
	en: async () => (await import('./en.json')).default,
	de: async () => (await import('./de.json')).default,
}

export function loadTranslations(language: Language) {
	return translationLoaders[language]()
}
