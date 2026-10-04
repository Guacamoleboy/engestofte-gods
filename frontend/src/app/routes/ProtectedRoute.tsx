// Pathing
// _______
// src/app/routes/ProtectedRoute.tsx

import { Navigate, Outlet, useLocation } from 'react-router-dom'
import { useAuth } from '../../shared/hooks/useAuth'
import { getDashboardPath, type AccountRole } from '../../shared/data/authSession'

type ProtectedRouteProps = {
	allowedRoles?: AccountRole[]
}

export default function ProtectedRoute({ allowedRoles }: ProtectedRouteProps) {
	const { isAuthenticated, user } = useAuth()
	const location = useLocation()

	if (!isAuthenticated) {
		return <Navigate to="/login" replace state={{ from: location }} />
	}
	if (allowedRoles && user && !allowedRoles.includes(user.role)) {
		return <Navigate to={getDashboardPath(user.role)} replace />
	}
	return <Outlet />
}
