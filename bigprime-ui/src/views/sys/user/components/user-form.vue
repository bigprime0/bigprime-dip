<template>
  <div class="ds-form">
    <tiny-form ref="formRef" overflow-title :model="formData" :rules="formRules" label-width="100px">
      <tiny-divider content-position="left" content-color="#7693f5">{{ $t('common.field.divider') }}
      </tiny-divider>

      <tiny-row>
        <tiny-col :span="6">
          <tiny-form-item :label="$t('sys.user.field.username')" :show-message="false" prop="username">
            <tiny-input v-model="formData.username" :disabled="formData.id > 0" :placeholder="$t('sys.user.field.default.password')"></tiny-input>
          </tiny-form-item>
        </tiny-col>
        <tiny-col :span="3">
          <tiny-form-item :label="$t('sys.user.field.status')">
            <tiny-switch v-model="formData.status" :true-value=1 :false-value=0 :disabled="isDialogBox"></tiny-switch>
          </tiny-form-item>
        </tiny-col>
        <tiny-col v-if="userStore.userInfo.superAdmin === 1" :span="3">
          <tiny-form-item :label="$t('sys.user.field.superAdmin')">
            <tiny-switch v-model="formData.superAdmin" :true-value=1 :false-value=0 :disabled="isDialogBox || userStore.userInfo.id === formData.id"></tiny-switch>
          </tiny-form-item>
        </tiny-col>
      </tiny-row>

      <tiny-row>
        <tiny-col :span="6">
          <tiny-form-item :label="$t('sys.user.field.orgName')" :show-message="false" prop="orgId">
            <OrgSelect v-model="formData.orgId" :disabled="isDialogBox" />
          </tiny-form-item>
        </tiny-col>
        <tiny-col :span="6">
          <tiny-form-item :label="$t('sys.user.field.roleIdList')" :show-message="false" prop="roleIdList">
            <RoleSelect v-model="formData.roleIdList" :multiple="true" :disabled="isDialogBox" />
          </tiny-form-item>
        </tiny-col>
      </tiny-row>

      <tiny-row>
        <tiny-col :span="6">
          <tiny-form-item :label="$t('sys.user.field.realName')" :show-message="false" prop="realName">
            <tiny-input v-model="formData.realName"></tiny-input>
          </tiny-form-item>
        </tiny-col>
        <tiny-col :span="6">
          <tiny-form-item :label="$t('sys.user.field.gender')">
            <tiny-radio v-model="formData.gender" :label=0>{{ $t('common.field.sex.man') }}</tiny-radio>
            <tiny-radio v-model="formData.gender" :label=1>{{ $t('common.field.sex.woman') }}</tiny-radio>
          </tiny-form-item>
        </tiny-col>
      </tiny-row>

      <tiny-row>
        <tiny-col :span="6">
          <tiny-form-item :label="$t('sys.user.field.mobile')">
            <tiny-input v-model="formData.mobile"></tiny-input>
          </tiny-form-item>
        </tiny-col>
        <tiny-col :span="6">
          <tiny-form-item :label="$t('sys.user.field.email')">
            <tiny-input v-model="formData.email"></tiny-input>
          </tiny-form-item>
        </tiny-col>
      </tiny-row>

      <br />
      <tiny-row style="text-align: center">
        <tiny-button plain type="primary" @click="handleCancel"> {{ $t('common.operations.cancel') }}
        </tiny-button>
        <tiny-button :reset-time="500" type="primary" @click="handleSave"> {{ $t('common.operations.save') }}
        </tiny-button>
      </tiny-row>
      <br />
      <br />
    </tiny-form>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import emitter from '@/utils/evnetbus'
import { useI18n } from 'vue-i18n'
import { Modal } from '@opentiny/vue'
import { type UserInfo } from '@/services/sys/sys-user'
import OrgSelect from '@/views/sys/org/components/org-select.vue'
import RoleSelect from '@/views/sys/role/components/role-select.vue'
import { useUserStore } from '@/store'

const userStore = useUserStore()
const { t } = useI18n()

const formRef = ref()
const formData = defineModel<UserInfo>('formData', { default: {} })
const isDialogBox = defineModel('isDialogBox', { default: false })


const formRules = computed(() => {
  let ruleList = {
    username: [{ required: true, message: t('common.prompt.required'), trigger: 'blur' }],
    realName: [{ required: true, message: t('common.prompt.required'), trigger: 'blur' }],
    orgId: [{ required: true, message: t('common.prompt.required'), trigger: 'blur' }],
    roleIdList: [{ required: true, message: t('common.prompt.required'), trigger: 'blur' }]
  }
  return ruleList
})

const handleSave = () => {
  formRef.value
    .validate()
    .then(async () => {
      await userStore.save(formData.value)
      Modal.message({
        message: t('common.prompt.save.success'),
        status: 'success'
      })
      handleCancel()
    })
}
const handleCancel = () => {
  emitter.emit('drawerCancel')
}


</script>

<style scoped lang="less">
.span-font {
  font-size: 13px;
  color: #3ac295;
}

.container-split {
  height: calc(100vh - 110px);
  border: 1px solid #d9d9d9;
}

.ds-choose {
  :deep(.tiny-collapse) {
    padding: 0 10px;
    border-top: 0;
    border-bottom: 0;
  }
}

.ds-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: normal;

  :deep(.tiny-card) {
    width: 100px;
  }

  :deep(.tiny-card.tiny-card--default) {
    margin: 3px;
    cursor: pointer;
  }

  :deep(.tiny-card.tiny-card--success) {
    border: 2px solid #3ac295;
  }

  :deep(img) {
    height: 50px;
  }

  :deep(.tiny-card .tiny-card__body .tiny-card--logo.tiny-card--mini-padding) {
    padding: 0px;
  }
}

.ds-form {
  padding: 10px;

  :deep(.container-form) {
    margin-top: 0;
    padding: 20px 15px 15px 15px;
    border-radius: 0;
    box-shadow: 0 0 2px 2px #eee;
    height: 100vh - 80px;
    height: calc(100vh - 130px);
  }

  :deep(.tiny-select__tags .tiny-select__tags-text.is-disabled>span) {
    color: #000000;
    font-size: var(--ti-tag-font-size);
    margin: var(--ti-select-tags-margin-top) var(--ti-select-tags-margin-right) var(--ti-select-tags-margin-bottom) var(--ti-select-tags-margin-left);
    display: inline-block;
    width: 100%;
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
  }
}
</style>
