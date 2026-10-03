// Pathing
// _______
// src/features/events-dashboard-page/DashboardNavigation.hooks.ts

import { useNavigate } from 'react-router-dom'
import { clearAuthSession, getAccountRole } from '../../shared/data/authSession'

export function useDashboardNavigation() {
	const navigate = useNavigate()
	const role = getAccountRole()

	function logout() {
		clearAuthSession()
		navigate('/kontakt', { replace: true })
	}

	return { logout, role }
}
