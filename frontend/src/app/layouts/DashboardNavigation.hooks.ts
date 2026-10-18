// Pathing
// _______
// src/app/layouts/DashboardNavigation.hooks.ts

import { useNavigate } from 'react-router-dom'
import { useAuth } from '../../shared/hooks/useAuth'

export function useDashboardNavigation() {
	
	// Setup
	const navigate = useNavigate()
	const { user, clearSession } = useAuth()
	const role = user?.role ?? null

	// Logout
	function logout() {
		clearSession()
		navigate('/kontakt', { replace: true })
	}

	// Return
	return { logout, role }
	
}