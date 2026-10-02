// Pathing
// _______
// src/shared/hooks/useTranslate.ts

import { useContext } from 'react'
import { TranslationContext } from '../context/TranslationContext'

export function useTranslate() {
	const context = useContext(TranslationContext)
	if (!context) throw new Error('useTranslate must be used within TranslationProvider')
	return context
}
