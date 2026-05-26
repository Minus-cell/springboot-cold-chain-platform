import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const state = {
  token: localStorage.getItem('token') || '',
  userInfo: JSON.parse(localStorage.getItem('userInfo') || 'null')
}

const mutations = {
  SET_TOKEN(state, token) {
    state.token = token
    localStorage.setItem('token', token)
  },
  SET_USER_INFO(state, userInfo) {
    state.userInfo = userInfo
    if (userInfo) {
      localStorage.setItem('userInfo', JSON.stringify(userInfo))
    } else {
      localStorage.removeItem('userInfo')
    }
  },
  CLEAR_STATE(state) {
    state.token = ''
    state.userInfo = null
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }
}

const actions = {
  login({ commit }, payload) {
    const { token, userInfo } = payload
    commit('SET_TOKEN', token)
    commit('SET_USER_INFO', userInfo)
  },
  logout({ commit }) {
    commit('CLEAR_STATE')
  },
  getUserInfo({ commit }) {
    return new Promise((resolve, reject) => {
      if (!state.token) {
        reject('No token')
        return
      }
      resolve(state.userInfo)
    })
  }
}

export default new Vuex.Store({
  state,
  mutations,
  actions
})