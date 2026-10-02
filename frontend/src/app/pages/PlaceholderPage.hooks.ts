// Pathing
// _______
// src/app/pages/PlaceholderPage.hooks.ts

import { useOutletContext } from 'react-router-dom'
import type { Language, TranslationContent } from '../../shared/data/i18n/types'

type PublicLayoutContext = {
	language: Language
	content: TranslationContent
}

export function usePageContent() {
	return useOutletContext<PublicLayoutContext>()
}
