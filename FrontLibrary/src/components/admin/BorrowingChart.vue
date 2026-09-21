<template>
  <section class="borrowing-card">

    <!-- =====================================================
         HEADER
    ====================================================== -->
    <div class="borrowing-header">

      <div>
        <h2>Borrowing Overview</h2>
        <p>{{ chartSubtitle }}</p>
      </div>

      <!-- PERIOD SWITCH -->
      <div class="period-switch">
        <button
          type="button"
          :class="{ active: viewType === 'year' }"
          @click="changeView('year')"
        >
          Yearly
        </button>

        <button
          type="button"
          :class="{ active: viewType === 'month' }"
          @click="changeView('month')"
        >
          Monthly
        </button>

        <button
          type="button"
          :class="{ active: viewType === 'week' }"
          @click="changeView('week')"
        >
          Weekly
        </button>

        <button
          type="button"
          :class="{ active: viewType === 'day' }"
          @click="changeView('day')"
        >
          Daily
        </button>
      </div>

    </div>


    <!-- =====================================================
         FILTER
    ====================================================== -->
    <div class="filter-row">

      <!-- DAILY -->
      <div
        v-if="viewType === 'day'"
        class="filter-control"
      >
        <label>Date</label>

        <input
          v-model="selectedDate"
          type="date"
        />
      </div>


      <!-- WEEKLY -->
      <div
        v-if="viewType === 'week'"
        class="filter-control"
      >
        <label>Week</label>

        <input
          v-model="selectedWeekDate"
          type="date"
        />
      </div>


      <!-- MONTHLY -->
      <div
        v-if="viewType === 'month'"
        class="filter-control"
      >
        <label>Month</label>

        <select v-model.number="selectedMonth">
          <option
            v-for="month in months"
            :key="month.value"
            :value="month.value"
          >
            {{ month.label }}
          </option>
        </select>
      </div>

      <div
        v-if="viewType === 'month'"
        class="filter-control"
      >
        <label>Year</label>

        <select v-model.number="selectedMonthYear">
          <option
            v-for="year in availableYears"
            :key="year"
            :value="year"
          >
            {{ year }}
          </option>
        </select>
      </div>


      <!-- YEARLY -->
      <template v-if="viewType === 'year'">

        <div class="filter-control">
          <label>From Year</label>

          <select v-model.number="fromYear">
            <option
              v-for="year in availableYears"
              :key="year"
              :value="year"
            >
              {{ year }}
            </option>
          </select>
        </div>

        <div class="filter-control">
          <label>To Year</label>

          <select v-model.number="toYear">
            <option
              v-for="year in availableYears"
              :key="year"
              :value="year"
            >
              {{ year }}
            </option>
          </select>
        </div>

      </template>


      <!-- REFRESH -->
      <button
        type="button"
        class="refresh-btn"
        :disabled="loading"
        @click="fetchBorrowings"
      >
        <i
          class="bi"
          :class="loading ? 'bi-arrow-repeat spin' : 'bi-arrow-clockwise'"
        ></i>

        Refresh
      </button>

    </div>


    <!-- =====================================================
         ERROR
    ====================================================== -->
    <div
      v-if="error"
      class="error-box"
    >
      <i class="bi bi-exclamation-circle"></i>
      <span>{{ error }}</span>
    </div>


    <!-- =====================================================
         LOADING
    ====================================================== -->
    <div
      v-if="loading"
      class="loading-state"
    >
      <div class="loading-spinner"></div>
      <span>Loading borrowing data...</span>
    </div>


    <!-- =====================================================
         CHART
    ====================================================== -->
    <div
      v-else
      class="chart-section"
    >

      <!-- CHART TITLE -->
      <div class="chart-period-title">
        {{ periodLabel }}
      </div>


      <div class="chart-layout">

        <!-- Y AXIS -->
        <div class="y-axis">

          <span
            v-for="label in yAxisLabels"
            :key="label"
          >
            {{ label }}
          </span>

        </div>


        <!-- CHART -->
        <div class="chart-scroll">

          <div
            class="chart-wrapper"
            :class="`chart-${viewType}`"
          >

            <svg
              class="chart-svg"
              viewBox="0 0 1000 320"
              preserveAspectRatio="none"
            >

              <!-- =================================================
                   GRID
              ================================================== -->
              <line
                v-for="y in gridLines"
                :key="y"
                x1="0"
                :y1="y"
                x2="1000"
                :y2="y"
                class="grid-line"
              />


              <!-- =================================================
                   AREA
              ================================================== -->
              <defs>

                <linearGradient
                  id="borrowingAreaGradient"
                  x1="0"
                  y1="0"
                  x2="0"
                  y2="1"
                >
                  <stop
                    offset="0%"
                    stop-opacity="0.25"
                  />

                  <stop
                    offset="100%"
                    stop-opacity="0.02"
                  />
                </linearGradient>

              </defs>


              <path
                v-if="areaPath"
                :d="areaPath"
                class="area-path"
              />


              <!-- =================================================
                   LINE
              ================================================== -->
              <path
                v-if="linePath"
                :d="linePath"
                class="line-path"
              />

            </svg>


            <!-- =================================================
                 X AXIS
            ================================================== -->
            <div class="x-axis">

              <span
                v-for="item in borrowingData"
                :key="item.key"
                :title="item.fullLabel"
              >
                {{ item.label }}
              </span>

            </div>

          </div>

        </div>

      </div>


      <!-- =====================================================
           EMPTY
      ====================================================== -->
      <div
        v-if="!loading && borrowingData.length === 0"
        class="empty-state"
      >
        <i class="bi bi-bar-chart"></i>
        <span>No borrowing data found.</span>
      </div>


      <!-- =====================================================
           FOOTER SUMMARY
      ====================================================== -->
      <div class="chart-footer">

        <div class="summary-item">
          <span>Total</span>
          <strong>{{ totalBorrowings }}</strong>
        </div>

        <div class="summary-item">
          <span>Highest</span>
          <strong>{{ peakBorrowings }}</strong>
        </div>

        <div class="summary-item">
          <span>Range</span>
          <strong>{{ periodLabel }}</strong>
        </div>

      </div>

    </div>

  </section>
</template>


<script setup>
import { ref, computed, onMounted } from "vue";
import { getBorrowings } from "../../service/borrowingService.js";


/* ============================================================
   DATE HELPERS
============================================================ */

const today = new Date();

const formatDateInput = (date) => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");

  return `${year}-${month}-${day}`;
};

const getDayKey = (date) => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");

  return `${year}-${month}-${day}`;
};

const getMonthKey = (date) => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");

  return `${year}-${month}`;
};

const parseDate = (value) => {
  if (!value) {
    return null;
  }

  const date = new Date(value);

  if (Number.isNaN(date.getTime())) {
    return null;
  }

  return date;
};


/* ============================================================
   WEEK HELPERS
============================================================ */

const getMonday = (date) => {
  const result = new Date(date);

  result.setHours(0, 0, 0, 0);

  const day = result.getDay();

  const diff = day === 0 ? -6 : 1 - day;

  result.setDate(result.getDate() + diff);

  return result;
};


/* ============================================================
   STATE
============================================================ */

const borrowings = ref([]);

const loading = ref(false);

const error = ref("");

const viewType = ref("month");


/* ============================================================
   DAILY
============================================================ */

const selectedDate = ref(
  formatDateInput(today)
);


/* ============================================================
   WEEKLY
============================================================ */

const selectedWeekDate = ref(
  formatDateInput(today)
);


/* ============================================================
   MONTHLY
============================================================ */

const selectedMonth = ref(
  today.getMonth() + 1
);

const selectedMonthYear = ref(
  today.getFullYear()
);


/* ============================================================
   YEARLY
============================================================ */

const fromYear = ref(
  today.getFullYear() - 4
);

const toYear = ref(
  today.getFullYear()
);


/* ============================================================
   MONTHS
============================================================ */

const months = [
  {
    value: 1,
    label: "January",
  },
  {
    value: 2,
    label: "February",
  },
  {
    value: 3,
    label: "March",
  },
  {
    value: 4,
    label: "April",
  },
  {
    value: 5,
    label: "May",
  },
  {
    value: 6,
    label: "June",
  },
  {
    value: 7,
    label: "July",
  },
  {
    value: 8,
    label: "August",
  },
  {
    value: 9,
    label: "September",
  },
  {
    value: 10,
    label: "October",
  },
  {
    value: 11,
    label: "November",
  },
  {
    value: 12,
    label: "December",
  },
];


/* ============================================================
   AVAILABLE YEARS
============================================================ */

const currentYear = today.getFullYear();

const availableYears = computed(() => {
  const years = [];

  for (
    let year = currentYear - 20;
    year <= currentYear + 1;
    year++
  ) {
    years.push(year);
  }

  return years;
});


/* ============================================================
   CHANGE VIEW
============================================================ */

const changeView = (value) => {
  viewType.value = value;

  error.value = "";
};


/* ============================================================
   EXTRACT API RESPONSE
============================================================ */

const extractBorrowings = (response) => {
  if (Array.isArray(response)) {
    return response;
  }

  if (Array.isArray(response?.data)) {
    return response.data;
  }

  if (Array.isArray(response?.data?.data)) {
    return response.data.data;
  }

  if (Array.isArray(response?.result)) {
    return response.result;
  }

  return [];
};


/* ============================================================
   GET BORROWING DATE/TIME
============================================================ */

const getBorrowingTimestamp = (item) => {
  return (
    item?.createdAt ||
    item?.createdDate ||
    item?.borrowedAt ||
    item?.borrowDateTime ||
    item?.borrowedDateTime ||
    item?.borrowDate ||
    item?.borrowedDate ||
    item?.borrowingDate ||
    null
  );
};


/* ============================================================
   FETCH BORROWINGS
============================================================ */

const fetchBorrowings = async () => {
  loading.value = true;
  error.value = "";

  try {
    const response = await getBorrowings();

    console.log("Borrowing Overview response:", response);

    borrowings.value = extractBorrowings(response);

    console.log(
      "Borrowing Overview data:",
      borrowings.value
    );

  } catch (err) {
    console.error(
      "Failed to fetch borrowing overview:",
      err
    );

    error.value =
      err?.message ||
      "Failed to load borrowing data.";

    borrowings.value = [];

  } finally {
    loading.value = false;
  }
};


/* ============================================================
   DAILY PERIODS
   00:00 → 23:00
============================================================ */

const makeDailyPeriods = () => {
  const periods = [];

  for (let hour = 0; hour < 24; hour++) {

    const hourText = String(hour).padStart(2, "0");

    periods.push({
      key: hourText,
      label: `${hourText}:00`,
      fullLabel: `${hourText}:00`,
    });

  }

  return periods;
};


/* ============================================================
   WEEKLY PERIODS
   MON → SUN
============================================================ */

const makeWeeklyPeriods = () => {
  const monday = getMonday(
    parseDate(selectedWeekDate.value) || today
  );

  const labels = [
    "Mon",
    "Tue",
    "Wed",
    "Thu",
    "Fri",
    "Sat",
    "Sun",
  ];

  const periods = [];

  for (let index = 0; index < 7; index++) {

    const date = new Date(monday);

    date.setDate(
      monday.getDate() + index
    );

    periods.push({
      key: getDayKey(date),
      label: labels[index],
      fullLabel: date.toLocaleDateString(
        undefined,
        {
          weekday: "long",
          month: "short",
          day: "numeric",
        }
      ),
    });

  }

  return periods;
};


/* ============================================================
   MONTHLY PERIODS
   DAY 1 → LAST DAY OF MONTH
============================================================ */

const makeMonthlyPeriods = () => {
  const year = selectedMonthYear.value;

  const monthIndex =
    selectedMonth.value - 1;

  const lastDay =
    new Date(
      year,
      monthIndex + 1,
      0
    ).getDate();

  const periods = [];

  for (
    let day = 1;
    day <= lastDay;
    day++
  ) {

    const date = new Date(
      year,
      monthIndex,
      day
    );

    periods.push({
      key: getDayKey(date),
      label: String(day).padStart(2, "0"),
      fullLabel: date.toLocaleDateString(
        undefined,
        {
          weekday: "short",
          month: "short",
          day: "numeric",
        }
      ),
    });

  }

  return periods;
};


/* ============================================================
   YEARLY PERIODS
============================================================ */

const makeYearlyPeriods = () => {

  const startYear = Math.min(
    Number(fromYear.value),
    Number(toYear.value)
  );

  const endYear = Math.max(
    Number(fromYear.value),
    Number(toYear.value)
  );

  /*
   * If only one year is selected:
   * show Jan → Dec
   */
  if (startYear === endYear) {

    return months.map((month) => ({
      key: `${startYear}-${String(month.value).padStart(2, "0")}`,
      label: month.label.substring(0, 3),
      fullLabel: `${month.label} ${startYear}`,
    }));

  }


  /*
   * Multiple years:
   * show each year
   */
  const periods = [];

  for (
    let year = startYear;
    year <= endYear;
    year++
  ) {

    periods.push({
      key: String(year),
      label: String(year),
      fullLabel: String(year),
    });

  }

  return periods;
};


/* ============================================================
   BUILD COUNTS
============================================================ */

const buildCounts = () => {

  const counts = new Map();


  /* ==========================================================
     DAILY
  ========================================================== */

  if (viewType.value === "day") {

    borrowings.value.forEach((item) => {

      const timestamp =
        getBorrowingTimestamp(item);

      const date = parseDate(timestamp);

      if (!date) {
        return;
      }

      const selected =
        selectedDate.value;

      const dateKey =
        getDayKey(date);

      if (dateKey !== selected) {
        return;
      }

      const hour =
        String(date.getHours()).padStart(
          2,
          "0"
        );

      counts.set(
        hour,
        (counts.get(hour) || 0) + 1
      );

    });

  }


  /* ==========================================================
     WEEKLY
  ========================================================== */

  if (viewType.value === "week") {

    const selected =
      parseDate(
        selectedWeekDate.value
      ) || today;

    const monday =
      getMonday(selected);

    const sunday =
      new Date(monday);

    sunday.setDate(
      monday.getDate() + 6
    );

    sunday.setHours(
      23,
      59,
      59,
      999
    );


    borrowings.value.forEach((item) => {

      const timestamp =
        getBorrowingTimestamp(item);

      const date =
        parseDate(timestamp);

      if (!date) {
        return;
      }

      if (
        date < monday ||
        date > sunday
      ) {
        return;
      }

      const key =
        getDayKey(date);

      counts.set(
        key,
        (counts.get(key) || 0) + 1
      );

    });

  }


  /* ==========================================================
     MONTHLY
     EACH DAY IN SELECTED MONTH
  ========================================================== */

  if (viewType.value === "month") {

    const year =
      Number(selectedMonthYear.value);

    const month =
      Number(selectedMonth.value) - 1;


    borrowings.value.forEach((item) => {

      const timestamp =
        getBorrowingTimestamp(item);

      const date =
        parseDate(timestamp);

      if (!date) {
        return;
      }


      if (
        date.getFullYear() !== year ||
        date.getMonth() !== month
      ) {
        return;
      }


      const key =
        getDayKey(date);

      counts.set(
        key,
        (counts.get(key) || 0) + 1
      );

    });

  }


  /* ==========================================================
     YEARLY
  ========================================================== */

  if (viewType.value === "year") {

    const startYear =
      Math.min(
        Number(fromYear.value),
        Number(toYear.value)
      );

    const endYear =
      Math.max(
        Number(fromYear.value),
        Number(toYear.value)
      );


    /*
     * ONE YEAR
     * Jan → Dec
     */
    if (startYear === endYear) {

      borrowings.value.forEach((item) => {

        const timestamp =
          getBorrowingTimestamp(item);

        const date =
          parseDate(timestamp);

        if (!date) {
          return;
        }

        if (
          date.getFullYear() !== startYear
        ) {
          return;
        }

        const key =
          getMonthKey(date);

        counts.set(
          key,
          (counts.get(key) || 0) + 1
        );

      });

    }


    /*
     * MULTIPLE YEARS
     * Year → Year
     */
    else {

      borrowings.value.forEach((item) => {

        const timestamp =
          getBorrowingTimestamp(item);

        const date =
          parseDate(timestamp);

        if (!date) {
          return;
        }

        const year =
          date.getFullYear();

        if (
          year < startYear ||
          year > endYear
        ) {
          return;
        }

        const key =
          String(year);

        counts.set(
          key,
          (counts.get(key) || 0) + 1
        );

      });

    }

  }


  return counts;
};


/* ============================================================
   BORROWING DATA
============================================================ */

const borrowingData = computed(() => {

  let periods = [];

  if (viewType.value === "day") {
    periods = makeDailyPeriods();
  }

  if (viewType.value === "week") {
    periods = makeWeeklyPeriods();
  }

  if (viewType.value === "month") {
    periods = makeMonthlyPeriods();
  }

  if (viewType.value === "year") {
    periods = makeYearlyPeriods();
  }


  const counts =
    buildCounts();


  return periods.map((period) => ({
    ...period,
    value: counts.get(period.key) || 0,
  }));

});


/* ============================================================
   TOTAL
============================================================ */

const totalBorrowings = computed(() => {

  return borrowingData.value.reduce(
    (total, item) =>
      total + item.value,
    0
  );

});


/* ============================================================
   PEAK
============================================================ */

const peakBorrowings = computed(() => {

  if (!borrowingData.value.length) {
    return 0;
  }

  return Math.max(
    ...borrowingData.value.map(
      (item) => item.value
    )
  );

});


/* ============================================================
   Y AXIS MAX
============================================================ */

const yAxisMax = computed(() => {

  const max =
    peakBorrowings.value;

  if (max <= 5) {
    return 5;
  }

  if (max <= 10) {
    return 10;
  }

  if (max <= 20) {
    return 20;
  }

  if (max <= 50) {
    return Math.ceil(max / 10) * 10;
  }

  if (max <= 100) {
    return Math.ceil(max / 20) * 20;
  }

  return Math.ceil(max / 50) * 50;

});


/* ============================================================
   Y AXIS LABELS
============================================================ */

const yAxisLabels = computed(() => {

  const max =
    yAxisMax.value;

  return [
    max,
    Math.round(max * 0.75),
    Math.round(max * 0.5),
    Math.round(max * 0.25),
    0,
  ];

});


/* ============================================================
   GRID LINES
============================================================ */

const gridLines = [
  30,
  102.5,
  175,
  247.5,
  320,
];


/* ============================================================
   CHART GEOMETRY
============================================================ */

const chartLeft = 12;

const chartRight = 988;

const chartTop = 25;

const chartBottom = 290;


/* ============================================================
   CREATE POINTS
============================================================ */

const chartPoints = computed(() => {

  const data =
    borrowingData.value;

  if (!data.length) {
    return [];
  }


  const width =
    chartRight - chartLeft;

  const height =
    chartBottom - chartTop;


  return data.map(
    (item, index) => {

      const x =
        data.length === 1
          ? chartLeft + width / 2
          : chartLeft +
            (index / (data.length - 1)) *
              width;


      const ratio =
        yAxisMax.value === 0
          ? 0
          : item.value /
            yAxisMax.value;


      const y =
        chartBottom -
        ratio * height;


      return {
        x,
        y,
        value: item.value,
      };

    }
  );

});


/* ============================================================
   SMOOTH PATH
============================================================ */

const createSmoothPath = (
  points
) => {

  if (!points.length) {
    return "";
  }

  if (points.length === 1) {
    return `M ${points[0].x} ${points[0].y}`;
  }


  let path =
    `M ${points[0].x} ${points[0].y}`;


  for (
    let i = 0;
    i < points.length - 1;
    i++
  ) {

    const current =
      points[i];

    const next =
      points[i + 1];

    const controlX =
      (current.x + next.x) / 2;


    path +=
      ` C ${controlX} ${current.y},
          ${controlX} ${next.y},
          ${next.x} ${next.y}`;

  }


  return path;
};


/* ============================================================
   LINE PATH
============================================================ */

const linePath = computed(() => {

  return createSmoothPath(
    chartPoints.value
  );

});


/* ============================================================
   AREA PATH
============================================================ */

const areaPath = computed(() => {

  const points =
    chartPoints.value;

  if (!points.length) {
    return "";
  }


  const line =
    createSmoothPath(points);


  return (
    line +
    ` L ${points[points.length - 1].x} ${chartBottom}` +
    ` L ${points[0].x} ${chartBottom}` +
    " Z"
  );

});


/* ============================================================
   CHART SUBTITLE
============================================================ */

const chartSubtitle = computed(() => {

  if (viewType.value === "day") {
    return "Hourly borrowing activity";
  }

  if (viewType.value === "week") {
    return "Daily borrowing activity";
  }

  if (viewType.value === "month") {
    return "Daily borrowing activity for selected month";
  }

  return "Monthly / yearly borrowing activity";
});


/* ============================================================
   PERIOD LABEL
============================================================ */

const periodLabel = computed(() => {

  /* DAILY */

  if (viewType.value === "day") {

    const date =
      parseDate(
        selectedDate.value
      );

    if (!date) {
      return selectedDate.value;
    }

    return date.toLocaleDateString(
      undefined,
      {
        weekday: "short",
        month: "short",
        day: "numeric",
        year: "numeric",
      }
    );

  }


  /* WEEKLY */

  if (viewType.value === "week") {

    const selected =
      parseDate(
        selectedWeekDate.value
      ) || today;

    const monday =
      getMonday(selected);

    const sunday =
      new Date(monday);

    sunday.setDate(
      monday.getDate() + 6
    );


    const start =
      monday.toLocaleDateString(
        undefined,
        {
          month: "short",
          day: "numeric",
        }
      );


    const end =
      sunday.toLocaleDateString(
        undefined,
        {
          month: "short",
          day: "numeric",
          year: "numeric",
        }
      );


    return `${start} - ${end}`;

  }


  /* MONTHLY */

  if (viewType.value === "month") {

    const month =
      months.find(
        (item) =>
          item.value ===
          Number(selectedMonth.value)
      );

    return `${month?.label || ""} ${selectedMonthYear.value}`;

  }


  /* YEARLY */

  const startYear =
    Math.min(
      Number(fromYear.value),
      Number(toYear.value)
    );

  const endYear =
    Math.max(
      Number(fromYear.value),
      Number(toYear.value)
    );


  if (startYear === endYear) {
    return `${startYear} — Jan to Dec`;
  }


  return `${startYear} — ${endYear}`;

});


/* ============================================================
   INITIAL FETCH
============================================================ */

onMounted(() => {
  fetchBorrowings();
});
</script>


<style scoped>
/* ============================================================
   MAIN CARD
============================================================ */

.borrowing-card {
  width: 100%;
  background: #ffffff;
  border: 1px solid #e7ebf1;
  border-radius: 16px;
  padding: 22px;
  box-shadow: 0 4px 18px rgba(15, 23, 42, 0.04);
}


/* ============================================================
   HEADER
============================================================ */

.borrowing-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
  margin-bottom: 18px;
}

.borrowing-header h2 {
  margin: 0;
  color: #1f2937;
  font-size: 18px;
  font-weight: 700;
}

.borrowing-header p {
  margin: 5px 0 0;
  color: #8b95a7;
  font-size: 13px;
}


/* ============================================================
   PERIOD SWITCH
============================================================ */

.period-switch {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 4px;
  background: #f4f7f9;
  border-radius: 9px;
}

.period-switch button {
  border: none;
  background: transparent;
  color: #7b8794;
  font-size: 12px;
  font-weight: 600;
  padding: 7px 12px;
  border-radius: 7px;
  cursor: pointer;
  transition: 0.2s ease;
}

.period-switch button:hover {
  color: #3f9d94;
}

.period-switch button.active {
  background: #49b4aa;
  color: #ffffff;
  box-shadow: 0 2px 5px rgba(73, 180, 170, 0.25);
}


/* ============================================================
   FILTER
============================================================ */

.filter-row {
  display: flex;
  align-items: flex-end;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 18px;
}

.filter-control {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.filter-control label {
  color: #7d8795;
  font-size: 11px;
  font-weight: 600;
}

.filter-control input,
.filter-control select {
  height: 32px;
  min-width: 125px;
  padding: 0 9px;
  border: 1px solid #dfe5eb;
  border-radius: 7px;
  background: #ffffff;
  color: #4b5563;
  font-size: 12px;
  outline: none;
}

.filter-control input:focus,
.filter-control select:focus {
  border-color: #65bdb5;
  box-shadow: 0 0 0 2px rgba(73, 180, 170, 0.08);
}


/* ============================================================
   REFRESH
============================================================ */

.refresh-btn {
  height: 32px;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 0 11px;
  border: 1px solid #dfe5eb;
  border-radius: 7px;
  background: #ffffff;
  color: #5d6877;
  font-size: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: 0.2s ease;
}

.refresh-btn:hover {
  border-color: #65bdb5;
  color: #3f9d94;
}

.refresh-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.spin {
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}


/* ============================================================
   ERROR
============================================================ */

.error-box {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 15px;
  padding: 10px 12px;
  border: 1px solid #f2caca;
  border-radius: 8px;
  background: #fff6f6;
  color: #c74c4c;
  font-size: 12px;
}


/* ============================================================
   LOADING
============================================================ */

.loading-state {
  min-height: 300px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10px;
  color: #8b95a7;
  font-size: 13px;
}

.loading-spinner {
  width: 26px;
  height: 26px;
  border: 3px solid #e5eeee;
  border-top-color: #49b4aa;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}


/* ============================================================
   CHART SECTION
============================================================ */

.chart-section {
  width: 100%;
}

.chart-period-title {
  margin-bottom: 10px;
  color: #687385;
  font-size: 12px;
  font-weight: 600;
}


/* ============================================================
   CHART LAYOUT
============================================================ */

.chart-layout {
  display: flex;
  width: 100%;
}

.y-axis {
  width: 35px;
  min-width: 35px;
  height: 265px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: flex-start;
  padding-top: 2px;
  padding-bottom: 1px;
}

.y-axis span {
  color: #a1aab7;
  font-size: 10px;
  line-height: 1;
}


/* ============================================================
   CHART SCROLL
============================================================ */

.chart-scroll {
  flex: 1;
  min-width: 0;
  overflow-x: auto;
  overflow-y: hidden;
  scrollbar-width: thin;
}

.chart-wrapper {
  width: 100%;
  min-width: 650px;
}


/* ============================================================
   MONTHLY / DAILY WIDTH
============================================================ */

.chart-month {
  min-width: 850px;
}

.chart-day {
  min-width: 850px;
}

.chart-week {
  min-width: 650px;
}

.chart-year {
  min-width: 650px;
}


/* ============================================================
   SVG
============================================================ */

.chart-svg {
  display: block;
  width: 100%;
  height: 265px;
  overflow: visible;
}

.grid-line {
  stroke: #edf0f3;
  stroke-width: 1;
  stroke-dasharray: 4 5;
}

.line-path {
  fill: none;
  stroke: #43aaa0;
  stroke-width: 4;
  stroke-linecap: round;
  stroke-linejoin: round;
}

.area-path {
  fill: url(#borrowingAreaGradient);
}


/* ============================================================
   X AXIS
============================================================ */

.x-axis {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
  padding: 9px 2px 0;
}

.x-axis span {
  flex: 1;
  min-width: 0;
  text-align: center;
  color: #98a1ae;
  font-size: 10px;
  white-space: nowrap;
}


/* ============================================================
   FOOTER
============================================================ */

.chart-footer {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-top: 20px;
  padding-top: 15px;
  border-top: 1px solid #edf0f3;
}

.summary-item {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.summary-item span {
  color: #98a1ae;
  font-size: 11px;
}

.summary-item strong {
  overflow: hidden;
  color: #374151;
  font-size: 14px;
  font-weight: 700;
  text-overflow: ellipsis;
  white-space: nowrap;
}


/* ============================================================
   EMPTY
============================================================ */

.empty-state {
  min-height: 150px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #9aa4b2;
  font-size: 13px;
}


/* ============================================================
   RESPONSIVE
============================================================ */

@media (max-width: 950px) {

  .borrowing-header {
    align-items: flex-start;
    flex-direction: column;
  }

  .period-switch {
    width: 100%;
    overflow-x: auto;
  }

  .period-switch button {
    flex: 1;
    white-space: nowrap;
  }

}


@media (max-width: 650px) {

  .borrowing-card {
    padding: 15px;
    border-radius: 13px;
  }

  .borrowing-header h2 {
    font-size: 16px;
  }

  .borrowing-header p {
    font-size: 12px;
  }

  .filter-row {
    align-items: stretch;
  }

  .filter-control {
    flex: 1;
    min-width: 120px;
  }

  .filter-control input,
  .filter-control select {
    width: 100%;
  }

  .refresh-btn {
    justify-content: center;
  }

  .chart-month,
  .chart-day {
    min-width: 900px;
  }

  .chart-footer {
    gap: 8px;
  }

  .summary-item strong {
    font-size: 13px;
  }

}
</style>