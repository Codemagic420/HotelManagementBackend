import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
  vus: __ENV.K6_VUS ? parseInt(__ENV.K6_VUS) : 50,
  duration: __ENV.K6_DURATION || '4m',
  thresholds: {
    'http_req_duration': ['p(95)<500'],
    'http_req_failed': ['rate<0.01'],
    'checks': ['rate>0.99'],
  },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export default function () {
  const res1 = http.get(`${BASE_URL}/api/rooms`);
  check(res1, { 'rooms status 200': (r) => r.status === 200 });
  sleep(1);

  const res2 = http.get(`${BASE_URL}/v3/api-docs`);
  check(res2, { 'openapi 200': (r) => r.status === 200 });
  sleep(1);

  const res3 = http.get(`${BASE_URL}/actuator/health`);
  check(res3, { 'health up': (r) => r.status === 200 });
  sleep(1);
}
