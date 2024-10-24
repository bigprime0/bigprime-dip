<template>
  <div class="ds-form">
    <tiny-form ref="formRef" overflow-title :model="formData" :rules="formRules" label-width="100px">
      <tiny-divider content-position="left" content-color="#7693f5">{{ $t('common.field.divider') }}
      </tiny-divider>

      <tiny-row>
        <tiny-col :span="6">
          <tiny-form-item :label="$t('sys.role.field.name')" :show-message="false" prop="name">
            <tiny-input v-model="formData.name"></tiny-input>
          </tiny-form-item>
        </tiny-col>
      </tiny-row>

      <tiny-row>
        <tiny-col :span="6">
          <tiny-form-item :label="$t('sys.role.field.remark')">
            <tiny-input v-model="formData.remark" type="textarea" maxlength="100"></tiny-input>
          </tiny-form-item>
        </tiny-col>
      </tiny-row>

      <tiny-row>
        <tiny-col :span="10">
          <tiny-form-item :label="$t('sys.role.field.menuIdList')">
            <MenuSelect v-model="formData.menuIdList" :multiple="true"/>
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
    </tiny-form>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch, watchEffect } from 'vue'
import emitter from '@/utils/evnetbus'
import { useI18n } from 'vue-i18n'
import { Modal } from '@opentiny/vue'
import { SysRoleService, type RoleInfo } from '@/services/sys/sys-role'
import MenuSelect from '@/views/sys/menu/components/menu-select.vue'


const { t } = useI18n()

const formRef = ref()
const formData = defineModel<RoleInfo>('formData', { default: {} })


const formRules = computed(() => {
  let ruleList = {
    name: [{ required: true, message: t('common.prompt.required'), trigger: 'blur' }]
  }
  return ruleList
})

const handleSave = () => {
  formRef.value
    .validate()
    .then(async () => {
      await SysRoleService.save(formData.value)
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
}
</style>
