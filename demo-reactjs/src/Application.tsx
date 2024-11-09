import { BrowserRouter, Route, Routes } from 'react-router-dom';
import React, { useMemo } from 'react';
import RegisterUser from './pages/RegisterUser';
import Login from './pages/Login/Login';
import { Main } from './pages/Main';
import { useSessionHandler } from './hooks/useSessionHandler';
import ErrorBoundary from 'antd/es/alert/ErrorBoundary';

const publicRoute = () => {
	return (
		<Routes>
			<Route path='/' element={<Main />}>
				<Route index element={<Login />} />
				<Route path='Register' element={<RegisterUser />} />
				<Route path='Login' element={<Login />} />
			</Route>
		</Routes>
	);
};

const Application = () => {
	const { sessionContext, loadSessionFromToken } = useSessionHandler();
	useMemo(() => {
		if (sessionContext == null) {
			loadSessionFromToken();
		}
	}, []);

	return (
		<ErrorBoundary
			description='Something when wrong, please contact an administrator'
			message='An unknown error ocurrs'
		>
			<BrowserRouter>{publicRoute()}</BrowserRouter>
		</ErrorBoundary>
	);
};

export default Application;