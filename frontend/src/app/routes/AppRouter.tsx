// Pathing
// _______
// src/app/routes/AppRouter.tsx

import { Navigate, Route, Routes } from 'react-router-dom'
import PublicLayout from '../layouts/PublicLayout'
import PlaceholderPage from '../pages/PlaceholderPage'
import AiFlowPage from '../../features/ai-flow-page'
import AiFlowRedirectPage from '../../features/ai-flow-redirect-page'
import ContactPage from '../../features/contact-page'
import AiFlowTransitionProvider from '../../features/ai-flow-transition/AiFlowTransitionProvider'
import AuthPage from '../../features/auth-page'

export default function AppRouter() {
	return (
		<AiFlowTransitionProvider>
			<Routes>
				<Route path="/ai-flow" element={<AiFlowPage />} />
				<Route path="/ai-flow/redirect" element={<AiFlowRedirectPage />} />
				<Route path="/dashboard/events/" element={<PlaceholderPage route="/dashboard/events/" />} />
				<Route path="/dashboard/events/:id" element={<PlaceholderPage route="/dashboard/events/:id" />} />
				<Route element={<PublicLayout />}>
					<Route path="/" element={<Navigate to="/kontakt" replace />} />
					<Route path="/kontakt" element={<ContactPage />} />
					<Route path="/login" element={<AuthPage mode="login" />} />
					<Route path="/register" element={<AuthPage mode="register" />} />
					<Route path="/forgot-password" element={<AuthPage mode="forgot-password" />} />
					<Route path="*" element={<PlaceholderPage route="404" />} />
				</Route>
			</Routes>
		</AiFlowTransitionProvider>
	)
}
