// Pathing
// _______
// src/app/layouts/DashboardLayout.tsx

import { Outlet } from 'react-router-dom'
import DashboardNavigation from './DashboardNavigation'
import styles from './DashboardLayout.module.css'

export default function DashboardLayout() {
	return (
		<div className={styles.shell}>
			<DashboardNavigation />
			<Outlet />
		</div>
	)
}
