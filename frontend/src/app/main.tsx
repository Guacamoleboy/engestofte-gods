// Pathing
// _______
// src/app/main.tsx

import React from 'react'
import ReactDOM from 'react-dom/client'
import { HelmetProvider } from 'react-helmet-async'
import { BrowserRouter } from 'react-router-dom'
import App from './App'
import { TranslationProvider } from '../shared/context/TranslationContext'
import { AuthProvider } from '../shared/context/AuthContext'
import '../shared/styles/global.css'

ReactDOM.createRoot(document.getElementById('root')!).render(
	<React.StrictMode>
		<HelmetProvider>
			<BrowserRouter>
				<AuthProvider>
					<TranslationProvider>
						<App />
					</TranslationProvider>
				</AuthProvider>
			</BrowserRouter>
		</HelmetProvider>
	</React.StrictMode>,
)
