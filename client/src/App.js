import React from 'react'
import { HashRouter as Router, Route, Switch, Redirect } from 'react-router-dom'
import loadable from './utils/loadable'
import 'animate.css'
import './styles/base.scss'
import './styles/App.scss'

const DefaultLayout = loadable(() => import(/* webpackChunkName: 'default' */ './containers'))

const App = () => (
    <Router>
        <Switch>
            <Route path='/' exact render={() => <Redirect to='/index' />} />
            <Route component={DefaultLayout} />
        </Switch>
    </Router>
)

export default App