import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const state = {
  token: localStorage.getItem('portal_token') || '',
  userInfo: null
}

const mutations = {
  SET_TOKEN(state, token) {
    state.token = token
    localStorage.setItem('portal_token', token)
  },
  SET_USER_INFO(state, userInfo) {
    state.userInfo = userInfo
  },
  CLEAR_STATE(state) {
    state.token = ''
    state.userInfo = null
    localStorage.removeItem('portal_token')
  }
}

const actions = {
  login({ commit }, token) {
    commit('SET_TOKEN', token)
  },
  logout({ commit }) {
    commit('CLEAR_STATE')
  },
  setUserInfo({ commit }, userInfo) {
    commit('SET_USER_INFO', userInfo)
  }
}

export default new Vuex.Store({
  state,
  mutations,
  actions
})
