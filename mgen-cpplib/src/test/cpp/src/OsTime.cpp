#include "OsTime.h"

#ifdef _WIN32

#include <Windows.h>

/*
 LARGE_INTEGER frequency;
 if (::QueryPerformanceFrequency(&frequency) == FALSE)
 throw "foo";

 LARGE_INTEGER start;
 if (::QueryPerformanceCounter(&start) == FALSE)
 throw "foo";

 // Calculation.


 LARGE_INTEGER end;
 if (::QueryPerformanceCounter(&end) == FALSE)
 throw "foo";

 double interval = static_cast<double>(end.QuadPart - start.QuadPart) / frequency.QuadPart
 */
class Watch {
public:

	Watch() {
		QueryPerformanceFrequency(&m_frequency);
		QueryPerformanceCounter(&m_startTime);
	}

	~Watch() {
	}

	double getTimeSeconds() {
		LARGE_INTEGER t;
		QueryPerformanceCounter(&t);
		return static_cast<double>(t.QuadPart - m_startTime.QuadPart) / m_frequency.QuadPart;
	}

private:
	LARGE_INTEGER m_frequency;
	LARGE_INTEGER m_startTime;
};

double getCurTimeSeconds() {
	static Watch m_watch;
	return m_watch.getTimeSeconds();
}

#else

#include <stdio.h>
#include <stdint.h>
#include <time.h>

static int64_t getTimeNanos() {
	struct timespec t;
	clock_gettime(CLOCK_MONOTONIC, &t);
	return (uint64_t(t.tv_sec) * 1000000000L) + t.tv_nsec;
}

static const uint64_t s_t0 = getTimeNanos();

double getCurTimeSeconds() {
	return double(getTimeNanos() - s_t0) / 1e9;
}

#endif
