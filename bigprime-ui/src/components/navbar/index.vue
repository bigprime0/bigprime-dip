<template>
  <div class="navbar">
    <div class="left-side">
      <div style="display: flex; align-items: center">
        <img style="max-height: 50px;"
             src="/bigprime.png"
             @click="jumpUrl"
        />
        <h5 @click="jumpUrl">Data Integration Platform</h5>
      </div>
    </div>
    <ul class="right-side">
      <li>
        <div class="divider"></div>
      </li>
      <li @click="changeLan">
        <span v-if="i18.locale.value === 'zhCN'" class="span-font">中文</span>
        <span v-else class="span-font">English</span>
        <iconChevronDown class="navbar-lan"/>
        <div v-if="lan" class="trigger-lan">
          <li
              v-for="(item, index) in locales"
              :key="index"
              :value="item.value"
              @click="changeLange(locales[index].value)"
          >{{ item.label }}
          </li
          >
        </div>
      </li>

      <li class="navbar-user">
        <tiny-user-head v-if="!isEmpty(userStore.userInfo.avatarUrl)" v-model="userStore.userInfo.avatarUrl"
                        type="image"
                        round min></tiny-user-head>
        <tiny-user-head v-else v-model="userStore.userInfo.realName" class="head-item" background-color="#3ac295"
                        color="#fff" type="label" round min></tiny-user-head>
        <div class="trigger-user">
          <li
              v-for="(item, index) in userBox"
              :key="index"
              :value="item.label"
              @click="switchUser(item.value)"
          >
            <iconWriting v-if="item.value === 1"></iconWriting>
            <iconUser v-if="item.value === 2"></iconUser>
            <iconCheckOut v-if="item.value === 4"></iconCheckOut>
            <span>{{ $t(item.label) }}</span>
          </li>
        </div>
      </li>
    </ul>
  </div>
  <tiny-dialog-box
      :visible="userBoxVisible"
      @update:visible="userBoxVisible = $event"
      max-height="500"
      :modal-append-to-body="false"
      :is-form-reset="false"
      :title="$t('messageBox.userSettings')"
      width="50%"
  >
    <DataForm :formData="userStore.userInfo" :isDialogBox="true"/>
  </tiny-dialog-box>
  <tiny-dialog-box
      :visible="pwdBoxVisible"
      @update:visible="pwdBoxVisible = $event"
      max-height="500"
      :modal-append-to-body="false"
      :is-form-reset="false"
      :title="$t('messageBox.changePassword')"
      width="30%"
  >
    <PasswordForm/>
  </tiny-dialog-box>
</template>

<script lang="ts" setup>
import {onMounted, ref} from 'vue'
import {useI18n} from 'vue-i18n'
import {UserHead as TinyUserHead} from '@opentiny/vue'
import {IconCheckOut, IconChevronDown, IconUser, IconWriting} from '@opentiny/vue-icon'
import {LOCALE_OPTIONS} from '@/locale'
import useLocale from '@/hooks/locale'
import useUser from '@/hooks/user'
import {isEmpty} from 'lodash-es'
import {useUserStore} from '@/store'
import DataForm from '@/views/sys/user/components/user-form.vue'
import PasswordForm from '@/views/sys/user/components/user-password.vue'
import emitter from '@/utils/evnetbus'


const i18 = useI18n()
const iconUser = IconUser()
const iconCheckOut = IconCheckOut()
const iconWriting = IconWriting()
const iconChevronDown = IconChevronDown()
const lan = ref(false)
const {logout} = useUser()
const {changeLocale, getLocale} = useLocale()
const locales = [...LOCALE_OPTIONS]
const userStore = useUserStore()
const userBoxVisible = ref(false)
const pwdBoxVisible = ref(false)

onMounted(() => {
  const lange = getLocale()
})

// 切换语言
const changeLan = () => {
  lan.value = !lan.value
}

const changeLange = (lange: any) => {
  changeLocale(lange)
}


// 用户设置
const userBox = [
  {label: 'messageBox.userSettings', value: 1},
  {label: 'messageBox.changePassword', value: 2},
  {label: 'messageBox.logout', value: 4}

]

const switchUser = (e: number) => {
  switch (e) {
    case 1:
      userBoxVisible.value = true
      break
    case 2:
      pwdBoxVisible.value = true
      break
    case 4:
      logout()
      break
    default:
  }
}

const handleDrawerClose = () => {
  userBoxVisible.value = false
  pwdBoxVisible.value = false
}

//监听抽屉子组件的取消
emitter.on('drawerCancel', handleDrawerClose)

// 点击图标跳转首页
const jumpUrl = () => {
  window.location.href = `${window.location.protocol}//${window.location.host}`
}

</script>

<style scoped lang="less">
.span-font {
  font-size: 13px;
}

.navbar {
  display: flex;
  justify-content: space-between;
  height: 100%;
  background-color: #fff;
  border-bottom: 1px solid var(--color-border);
}

#navbar-search {
  width: 159px;
  height: 30px;
  padding: 14px;
  background-color: #eff1f7;
  background-position: 130px 8px;
  border: none;
  border-radius: 16px;
  outline: none;
}

.input-icon {
  background: url('@/assets/images/search.png') no-repeat scroll right center transparent;
}

.divider {
  width: 1px;
  height: 18px;
  margin: 0 2px;
  margin-top: 1px;
  background: #dfe1e6;
}

.left-side {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 60px;
  padding-left: 10px;
  cursor: pointer;

  .vue-icon {
    width: 24px;
    height: 24px;
    margin-left: 30px;
  }

  h5 {
    margin: 0 30px 0 12px;
    color: var(--ti-base-common-title-color);
    font-weight: 700;
    font-size: 22px;
    line-height: 32px;
    letter-spacing: 0.55px;
  }

  h4 {
    width: 135px;
    height: 22px;
    margin-left: 10px;
    color: var(--ti-base-common-title-color);
    font-weight: normal;
    font-size: 16px;
    line-height: 22px;
    text-align: left;
  }
}

.right-side {
  display: flex;
  padding-right: 20px;
  list-style: none;

  :deep(.locale-select) {
    border-radius: 20px;
  }

  li {
    display: flex;
    align-items: center;
    padding: 0 15px;

    .item {
      display: inline-block;
      margin: 4px;
    }

    span {
      cursor: pointer;
    }

    span:hover {
      color: #2f5bea;
    }

    .navbar-lan {
      padding: 0 0 0 5px;
    }
  }

  .user-image {
    display: flex;
    flex-direction: column;
    height: 100%;
    font-weight: 600;
    font-size: 2em;
    font-style: oblique;
    cursor: pointer;
    fill: var(--ti-common-color-line-active);
  }

  a {
    color: var(--color-text-1);
    text-decoration: none;
  }

  .trigger-lan {
    position: absolute;
    bottom: -44px;
    width: 100px;
    margin-left: -35px;
  }

  .navbar-user:hover > .trigger-user {
    display: inline-block;
  }

  .trigger-user {
    position: absolute;
    bottom: -80px;
    display: none;
    width: 100px;
    margin-left: -43px;
  }

  .trigger-user:hover {
    display: inline-block;
  }

  .trigger-user {
    li {
      display: flex;
      justify-content: space-around;
      padding: 6px;
      font-size: 12px;
      text-align: center;
      list-style-type: none;
      background-color: #fff;
      box-shadow: 0 0 2px 2px var(--ti-common-color-bg-normal);
      cursor: pointer;
    }

    li:hover {
      color: #2f5bea;
      background-color: #f5f6f7;
    }
  }

  .trigger-lan {
    li {
      display: block;
      padding: 6px;
      font-size: 12px;
      text-align: center;
      list-style-type: none;
      background-color: #fff;
      box-shadow: 0 0 2px 2px var(--ti-common-color-bg-normal);
      cursor: pointer;
    }

    li:hover {
      color: #2f5bea;
      background-color: #f5f6f7;
    }
  }
}
</style>

<style lang="less" scoped>
// responsive
@media (max-width: @screen-ms) {
  .left-side {
    img {
      width: 38px;
    }
  }

  .right-side {
    display: none;
  }
}

@media (max-width: @screen-mm) {
  .left-side {
    img {
      width: 38px;
    }
  }

  .right-side {
    display: none;
  }
}
</style>
