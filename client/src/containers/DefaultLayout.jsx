import React, {Component} from 'react'
import {Route, Switch, Redirect, withRouter} from 'react-router-dom'
import {connect} from 'react-redux'
import {Layout, BackTop, message} from 'antd'
import routes from '@/routes'
import {menuToggleAction} from '@/store/actionCreators'
import echarts from 'echarts/lib/echarts'
import avatar from '@/assets/images/user.jpg'
import menu from './menu'
import '@/styles/layout.scss'

import AppHeader from './AppHeader.jsx'
import AppAside from './AppAside.jsx'
import AppFooter from './AppFooter.jsx'

const {Content} = Layout

class DefaultLayout extends Component {
    state = {
        avatar,
        show: true,
        menu: []
    }

    loadMenu = () => {
        this.setState({
            menu: this.getMenu(menu)
        })
    }

    getMenu = menu => {
            return menu
    }

    componentDidMount() {
        this.loadMenu()
    }

    componentDidUpdate() {
        let {pathname} = this.props.location

        // 菜单收缩展开时 echarts 图表的自适应
        if (pathname === '/' || pathname === '/index') {
            this.timer = setTimeout(() => {
                echarts.init(document.getElementById('bar')).resize()
                echarts.init(document.getElementById('line')).resize()
                echarts.init(document.getElementById('pie')).resize()
                echarts.init(document.getElementById('pictorialBar')).resize()
                echarts.init(document.getElementById('scatter')).resize()
            }, 500)
        } else {
            this.timer = null
        }
    }

    componentWillUnmount() {
        this.timer && clearTimeout(this.timer)
    }

    render() {
        let {menuClick, menuToggle} = this.props
        // let {auth} = JSON.parse(localStorage.getItem('user')) ? JSON.parse(localStorage.getItem('user')) : ''
        return (
            <Layout className='app'>
                <BackTop/>
                <AppAside menuToggle={menuToggle} menu={this.state.menu}/>
                <Layout style={{marginLeft: menuToggle ? '80px' : '200px', minHeight: '100vh'}}>
                    <AppHeader
                        menuToggle={menuToggle}
                        menuClick={menuClick}
                        avatar={this.state.avatar}
                        show={this.state.show}
                        loginOut={this.loginOut}
                    />
                    <Content className='content'>
                        <Switch>
                            {routes.map(item => {
                                return (
                                    <Route
                                        key={item.path}
                                        path={item.path}
                                        exact={item.exact}/>
                                )
                            })}
                        </Switch>
                    </Content>
                    <AppFooter/>
                </Layout>
            </Layout>
        )
    }
}

const stateToProp = state => ({
    menuToggle: state.menuToggle
})

const dispatchToProp = dispatch => ({
    menuClick() {
        dispatch(menuToggleAction())
    }
})

export default withRouter(
    connect(
        stateToProp,
        dispatchToProp
    )(DefaultLayout)
)
