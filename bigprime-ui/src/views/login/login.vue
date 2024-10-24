<template>
  <div class="container-login">
    <div class="content">
      <div class="login">
        <div class="login-header">
          <div class="login-logo">
            <span class="login-logo-text">Data Integration Platform</span>
          </div>
          <div class="login-desc">{{ $t('login.main.text') }}</div>
        </div>
        <div class="login-form-container">
          <tiny-form
              ref="loginForm"
              :model="login"
              class="login-form"
              :rules="rules"
              validate-type="text"
              label-width="0"
          >
            <tiny-form-item :show-message="false" prop="username">
              <tiny-input
                  v-model="login.username"
                  :placeholder="$t('login.form.userName.placeholder')"
              >
              </tiny-input>
            </tiny-form-item>

            <tiny-form-item :show-message="false" prop="password">
              <tiny-input
                  v-model="login.password"
                  type="password"
                  show-password
                  :placeholder="$t('login.form.password.placeholder')"
              >
              </tiny-input>
            </tiny-form-item>

            <tiny-form-item span="2">
              <tiny-select v-model="locale" @change="changeLocale(locale)">
                <tiny-option v-for="item in locales" :key="item.value" :label="item.label" :value="item.value">
                </tiny-option>
              </tiny-select>
            </tiny-form-item>

            <tiny-form-item size="medium">
              <tiny-button
                  type="primary"
                  class="login-form-btn"
                  :loading="loading"
                  @click="handleSubmit"
              >{{ $t('login.form.login') }}
              </tiny-button
              >
            </tiny-form-item>
          </tiny-form>
        </div>
      </div>
    </div>
  </div>

</template>

<script lang="ts" setup>
import {onMounted, reactive, ref} from 'vue'
import {useRouter} from 'vue-router'
import {
  Button as TinyButton,
  Form as TinyForm,
  FormItem as TinyFormItem,
  Input as TinyInput,
  Modal
} from '@opentiny/vue'
import {useI18n} from 'vue-i18n'
import useLoading from '@/hooks/loading'
import useUserStore from '@/store/modules/user'
import {getToken} from '@/utils/token'
import {useRouterStore} from '@/store/modules/router'
import {isEmpty} from 'lodash-es'
import {LOCALE_OPTIONS} from '@/locale'
import useLocale from '@/hooks/locale'

const {changeLocale, getLocale} = useLocale()

const locales = [...LOCALE_OPTIONS]
const router = useRouter()
const {t} = useI18n()
const {loading, setLoading} = useLoading()
const loginForm = ref()
const userStore = useUserStore()
const routerStore = useRouterStore()

const locale = ref(getLocale())

onMounted(() => {
  if (getToken()) {
    router.push({name: 'home'})
  }
})

const rules = ref({
  username: {required: true, message: t('login.form.userName.errMsg'), trigger: 'change'},
  password: {required: true, message: t('login.form.password.errMsg'), trigger: 'change'}
})

const login = reactive({
  username: 'admin',
  password: 'admin',
  rememberPassword: true,
  captcha: '',
  key: ''
})

function handleSubmit() {
  loginForm.value?.validate(async (valid: boolean) => {
    if (!valid) {
      return
    }
    setLoading(true)
    try {
      await userStore.login({
        username: login.username,
        password: login.password
      })
      await routerStore.setRouters()
      if (isEmpty(routerStore.routers)) {
        //没有菜单权限
        Modal.message({
          message: t('setting.no.menu'),
          status: 'error'
        })
        await userStore.logout()
      } else {
        //跳转首页
        await router.push({name: 'home'})
      }
    } finally {
      setLoading(false)
    }
  })
}

</script>

<style lang="less" scoped>

.container-login {
  display: flex;
  flex-direction: column;
  width: 100vw;
  height: 100vh;
  background-image: url('@/assets/images/img_log2.png');
  background-size: 100% 100%;
}

.content {
  //margin-left: 100vh;
  margin-top: -10vh;
  position: relative;
  display: flex;
  flex: 1;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 0;
}

.login {
  width: 450px;
  height: 420px;
  padding: 60px 40px;
  font-size: var(--ti-common-font-size-1);
  //background: rgba(0,47,167, 1);
  background: rgba(255,255,255, 0.9);
  //box-shadow: 0 0 2px 2px var(--ti-common-color-bg-normal);

  &-header {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }

  &-logo {
/*    margin-right: 20px;*/

    &-img {
      margin-right: 10px;
      vertical-align: middle;
    }

    &-text {
      display: inline-block;
      //color: #002FA7;
      color: #002FA7;
      font-weight: bold;
      font-size: 30px;
      vertical-align: middle;
    }
  }

  &-desc {
    margin-top: 12px;
    margin-bottom: 20px;
    color: #002FA7;
    font-size: 14px;
  }
}

// 登录页面的tiny-link不保留hover后的下划线展示
:deep(.tiny-link.is-underline:hover::after) {
  border-bottom: none;
}


.login-form-container {
  margin-top: 5%;
}

.login-form {
  margin-left: 6%;

  .tiny-form-item {
    margin-bottom: 20px;
  }

  &-container {
    width: 320px;
  }

  &-options {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 20px;
    font-size: 12px;
  }

  &-btn {
    display: block;
    width: 100%;
    max-width: 100%;
    border-color: #002FA7;
    background-color: #002FA7;
  }
  &-btn:hover {
    display: block;
    width: 100%;
    max-width: 100%;
    border-color: #002FA7;
    background-color: #002FA7;
  }
}

.divide-line {
  margin: 0 5px;
}

// responsive
@media (max-width: @screen-ms) {
  .login-form {
    margin-left: 5%;

    &-container {
      width: 240px;
    }
  }
}
</style>
