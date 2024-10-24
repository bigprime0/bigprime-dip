<template>
  <div class="ds-form">
    <tiny-form ref="formRef" overflow-title :model="formData" :rules="formRules" label-width="100px">

      <tiny-row>
        <tiny-col>
          <tiny-form-item :label="$t('sys.user.field.password')" :show-message="false" prop="password">
            <tiny-input v-model="formData.password" type="password" show-password></tiny-input>
          </tiny-form-item>
        </tiny-col>
      </tiny-row>

      <tiny-row>
        <tiny-col>
          <tiny-form-item :label="$t('sys.user.field.newPassword')" :show-message="false" prop="newPassword">
            <tiny-input v-model="formData.newPassword" type="password" show-password></tiny-input>
          </tiny-form-item>
        </tiny-col>
      </tiny-row>

      <br />
      <tiny-row style="text-align: center">
        <tiny-button plain type="primary" @click="handleCancel"> {{ $t('common.operations.cancel') }}
        </tiny-button>
        <tiny-button :reset-time="500" type="primary" @click="handleSave"> {{ $t('common.operations.update') }}
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
import { SysUserService } from '@/services/sys/sys-user'
const { t } = useI18n()

const formRef = ref()
const formData = ref({ newPassword: '', password: '' })


const formRules = computed(() => {
  let ruleList = {
    password: [{ required: true, message: '必填', trigger: 'blur' }],
    newPassword: [{ required: true, message: '必填', trigger: 'blur' }]
  }
  return ruleList
})

const handleSave = () => {
  formRef.value
    .validate()
    .then(async () => {
      const result = await SysUserService.password(formData.value)
      if(result){
        Modal.message({
          message: t('user.password.success'),
          status: 'success'
        })
        handleCancel()
      }else{
        Modal.message({
          message: t('user.password.error'),
          status: 'error'
        })
      }

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

  :deep(.tiny-select__tags .tiny-select__tags-text.is-disabled>span){
    color:#000000;
    font-size: var(--ti-tag-font-size);
    margin: var(--ti-select-tags-margin-top) var(--ti-select-tags-margin-right) var(--ti-select-tags-margin-bottom) var(--ti-select-tags-margin-left);
    display: inline-block;
    width: 100%;
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
  }

  :deep(.tiny-row){
    width: 100%;
    margin-left: -28px;
  }
}
</style>
