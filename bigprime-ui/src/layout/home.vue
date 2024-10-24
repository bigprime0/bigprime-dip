<template>
  <div class="container-list">

    <div class="div-card">
      <tiny-card class="card-two">
        <template #title>
          <tiny-link class="card-title" :underline="false">{{ t('menu.integration') }}
          </tiny-link>
        </template>
        <template #default>
          <div class="div-card">
            <tiny-card class="card-two-info">
              <template #title>
                <tiny-link class="card-title-info" :underline="false" style="color: #c4891a">{{
                    t('home.numberOfTasks')
                  }}
                </tiny-link>
              </template>
              <template #default>
                <tiny-layout cols="3">
                  <tiny-row class="card-row">
                    <tiny-col :span="1">
                      <IconRichTextTaskList class="card-row-icon" style="fill: #c4891a"/>
                    </tiny-col>
                    <tiny-col :span="2">
                      <tiny-link class="card-col-value" :underline="false" style="color: #c4891a">
                        {{ statisticData.integration.numberOfTasks }}
                      </tiny-link>
                    </tiny-col>
                  </tiny-row>
                </tiny-layout>
              </template>
            </tiny-card>
            <tiny-card class="card-two-info">
              <template #title>
                <tiny-link class="card-title-info" :underline="false" style="color: #2c5048">{{ t('home.instances') }}
                </tiny-link>
              </template>
              <template #default>
                <tiny-layout cols="3">
                  <tiny-row class="card-row">
                    <tiny-col :span="1">
                      <IconHalfchecked class="card-row-icon" style="fill: #2c5048"/>
                    </tiny-col>
                    <tiny-col :span="2">
                      <tiny-link class="card-col-value" :underline="false" style="color: #2c5048">
                        {{ statisticData.integration.instances }}
                      </tiny-link>
                    </tiny-col>
                  </tiny-row>
                </tiny-layout>
              </template>
            </tiny-card>
            <tiny-card class="card-two-info">
              <template #title>
                <tiny-link class="card-title-info" :underline="false" style="color: #1ac456">{{ t('home.inOperation') }}
                </tiny-link>
              </template>
              <template #default>
                <tiny-layout cols="3">
                  <tiny-row class="card-row">
                    <tiny-col :span="1">
                      <IconStart class="card-row-icon" style="fill: #1ac456"/>
                    </tiny-col>
                    <tiny-col :span="2">
                      <tiny-link class="card-col-value" :underline="false" style="color: #1ac456">
                        {{ statisticData.integration.inOperation }}
                      </tiny-link>
                    </tiny-col>
                  </tiny-row>
                </tiny-layout>
              </template>
            </tiny-card>
            <tiny-card class="card-two-info">
              <template #title>
                <tiny-link class="card-title-info" :underline="false" style="color: #002FA7">{{
                    t('home.successNumbers')
                  }}
                </tiny-link>
              </template>
              <template #default>
                <tiny-layout cols="3">
                  <tiny-row class="card-row">
                    <tiny-col :span="1">
                      <IconSuccessful class="card-row-icon" style="fill: #002FA7"/>
                    </tiny-col>
                    <tiny-col :span="2">
                      <tiny-link class="card-col-value" :underline="false" style="color: #002FA7">
                        {{ statisticData.integration.successNumbers }}
                      </tiny-link>
                    </tiny-col>
                  </tiny-row>
                </tiny-layout>
              </template>
            </tiny-card>
            <tiny-card class="card-two-info">
              <template #title>
                <tiny-link class="card-title-info" :underline="false" style="color: red">{{
                    t('home.numberOfFailures')
                  }}
                </tiny-link>
              </template>
              <template #default>
                <tiny-layout cols="3">
                  <tiny-row class="card-row">
                    <tiny-col :span="1">
                      <IconOperationfaild class="card-row-icon" style="fill: red"/>
                    </tiny-col>
                    <tiny-col :span="2">
                      <tiny-link class="card-col-value" :underline="false" style="color: red">
                        {{ statisticData.integration.numberOfFailures }}
                      </tiny-link>
                    </tiny-col>
                  </tiny-row>
                </tiny-layout>
              </template>
            </tiny-card>
          </div>
        </template>
      </tiny-card>
    </div>

    <div class="div-card">
      <tiny-card class="card-chart">
        <template #title>
          <tiny-link class="card-title" :underline="false">{{ t('menu.dsm') }}
          </tiny-link>
        </template>
        <template #default>
          <ChartLine :data="sourceData"/>
        </template>
      </tiny-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue'
import {useI18n} from 'vue-i18n'
import ChartLine from '@/views/components/chart/chart-line.vue'
import {iconHalfchecked, iconOperationfaild, iconRichTextTaskList, iconStart, iconSuccessful} from '@opentiny/vue-icon'
import {CurrencyService} from '@/services/currency/currency'

const IconRichTextTaskList = iconRichTextTaskList()
const IconHalfchecked = iconHalfchecked()
const IconStart = iconStart()
const IconSuccessful = iconSuccessful()
const IconOperationfaild = iconOperationfaild()
const {t} = useI18n()

const statisticData = ref({
  integration: {
    numberOfTasks: 0,
    instances: 0,
    inOperation: 0,
    successNumbers: 0,
    numberOfFailures: 0
  },
  source: []
})

const sourceData = ref()
const filedMapping: Record<string, string> = {
  'sourceType': 'home.sourceType',
  'volume': 'home.volume',
  'tableQuantity': 'home.tableQuantity',
  'rows': 'home.rows',
  'bindNumber': 'home.binding.quantity',
  'bindSource': 'home.bind.data.source'
}

const getChartStatistic = (data: []) => {
  const result: any = []
  for (let i = 0; i < data.length; i++) {
    let object: any = {}
    for (const [key, value] of Object.entries(data[i])) {
      if (key in filedMapping) {
        object[t(filedMapping[key])] = value
      } else {
        object[key] = value
      }
    }
    result.push(object)
  }
  return result
}

const search = async () => {
  statisticData.value = await CurrencyService.getHomeStatistic()
  sourceData.value = getChartStatistic(statisticData.value.source)
}

onMounted(() => {
  search()
})

</script>
<style scoped lang="less">

.div-card {
  display: flex;
  margin: 10px 0 0 10px;
}

.card-chart {
  width: 100%;
  margin-right: 10px;
}

.card-two {
  width: 100%;
  height: 190px;
  margin-right: 10px;
}

.card-two-info {
  width: 20%;
  height: 100px;
  margin: 10px 10px 0 0;
}

.card-title {
  font-size: 15px;
  color: #3ac295;
}

.card-title-info {
  font-size: 13px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.card-row {
  margin-top: 10px;
  text-align: left;
  padding-left: 0px;
}

.card-row-icon {
  float: right;
  margin-top: 2px;
  width: 2em;
  height: 2em;
  vertical-align: middle;
  overflow: auto;
  display: inline-block;
}

.card-col-value {
  font-size: 16px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.card-col-label {
  font-size: 18px;
  //color: #002FA7;
}

.container-list {
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow-x: hidden;
  overflow-y: auto;
}

</style>

