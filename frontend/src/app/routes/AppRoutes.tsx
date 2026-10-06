// Pathing
// _______
// src/app/routes/AppRoutes.tsx

import { Navigate, Route, Routes } from 'react-router-dom'
import PublicLayout from '../layouts/PublicLayout'
import DashboardLayout from '../layouts/DashboardLayout'
import AiFlowLayout from '../layouts/AiFlowLayout'
import PlaceholderPage from '../pages/PlaceholderPage'
import EventsDashboardPage from '../pages/EventsDashboardPage'
import EventPage from '../pages/EventPage'
import CustomerApprovalPage from '../pages/CustomerApprovalPage'
import OwnerRequestsPage from '../pages/OwnerRequestsPage'
import OwnerRequestPage from '../pages/OwnerRequestPage'
import StaffEventsPage from '../../features/staff-events-page/StaffEventsPage'
import AiFlowPage from '../../features/ai-flow-page'
import AiFlowRedirectPage from '../../features/ai-flow-redirect-page'
import ContactPage from '../../features/contact-page'
import AiFlowTransitionProvider from '../../features/ai-flow-transition/AiFlowTransitionProvider'
import AuthPage from '../../features/auth-page'
import ProtectedRoute from './ProtectedRoute'

export default function AppRoutes() {
	return (
		<AiFlowTransitionProvider>
			<Routes>
				<Route element={<PublicLayout />}>
					<Route path="/" element={<Navigate to="/kontakt" replace />} />
					<Route path="/kontakt" element={<ContactPage />} />
					<Route path="/login" element={<AuthPage mode="login" />} />
					<Route path="/register" element={<AuthPage mode="register" />} />
					<Route path="/forgot-password" element={<AuthPage mode="forgot-password" />} />
					<Route path="*" element={<PlaceholderPage route="404" />} />
				</Route>
				<Route element={<AiFlowLayout />}>
					<Route path="/ai-flow" element={<AiFlowPage />} />
					<Route path="/ai-flow/redirect" element={<AiFlowRedirectPage />} />
				</Route>
				<Route element={<ProtectedRoute allowedRoles={['CUSTOMER']} />}>
					<Route element={<DashboardLayout />}>
						<Route path="/dashboard/events/" element={<EventsDashboardPage />} />
						<Route path="/dashboard/bookings" element={<EventsDashboardPage />} />
						<Route path="/dashboard/approval/:id" element={<CustomerApprovalPage />} />
						<Route path="/dashboard/events/:id" element={<EventPage />} />
					</Route>
				</Route>
				<Route element={<ProtectedRoute allowedRoles={['OWNER']} />}>
					<Route element={<DashboardLayout />}>
						<Route path="/owner/requests" element={<OwnerRequestsPage />} />
						<Route path="/owner/bookings" element={<OwnerRequestsPage />} />
						<Route path="/owner/requests/:id" element={<OwnerRequestPage />} />
						<Route path="/owner/events/:id" element={<EventPage />} />
					</Route>
				</Route>
				<Route element={<ProtectedRoute allowedRoles={['STAFF']} />}>
					<Route element={<DashboardLayout />}>
						<Route path="/staff/events" element={<StaffEventsPage />} />
						<Route path="/staff/events/:id" element={<EventPage />} />
					</Route>
				</Route>
			</Routes>
		</AiFlowTransitionProvider>
	)
}
